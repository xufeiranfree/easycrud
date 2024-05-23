package io.easycrud.core.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.easycrud.core.base.entity.*;
import io.easycrud.core.base.exception.BaseException;
import io.easycrud.core.base.exception.ExceptionEnum;
import io.easycrud.core.base.objectmapper.BaseObjectMapper;
import io.easycrud.core.base.repository.BaseRepository;
import io.easycrud.core.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BaseServiceImpl<DTO extends BaseDTO, VO extends BaseVO, DO extends BaseDO, PO extends BasePO> implements BaseService<DTO, VO> {

    @Autowired
    private BaseRepository<DO> baseRepository;

    @Autowired
    private BaseObjectMapper<DTO, VO, DO, PO> baseObjectMapper;

    @Override
    public Collection<VO> find() {
        List<DO> dos = baseRepository.findAll();
        return baseObjectMapper.doToVOs(dos);
    }

    @Override
    public Optional<VO> create(@NotNull DTO dto) {
        createControl(dto);
        DO aDo = baseObjectMapper.dtoToDO(dto);
        DO saved = baseRepository.save(aDo);
        VO vo = baseObjectMapper.doToVO(saved);
        return Optional.ofNullable(vo);
    }

    @Override
    public Optional<VO> find(@NotBlank String id) {
        Optional<DO> doOpt = baseRepository.findById(id);
        return doOpt.map(baseObjectMapper::doToVO);
    }

    @Override
    @Transactional
    public Optional<VO> update(@NotBlank String id, @NotNull DTO dto, boolean selective) {

        Optional<DO> existOpt = baseRepository.findById(id);
        existOpt.orElseThrow(() -> BaseException.builder()
                .exceptionEnum(ExceptionEnum.BAD_UPDATE_FOR_NOT_FOUNDED_RECORD)
                .build());
        DO exist = existOpt.get();

        DO aDo = baseObjectMapper.dtoToDO(dto);
        aDo.setId(id);
        DO saved;

        if (selective) {
            BeanUtil.copyProperties(aDo, exist, CopyOptions.create().ignoreNullValue());
            saved = baseRepository.save(exist);
        } else {
            saved = baseRepository.save(aDo);
        }

        return Optional.of(saved).map(baseObjectMapper::doToVO);
    }

    @Override
    public void remove(@NotBlank String id) {
        baseRepository.deleteById(id);
    }

    protected void createControl(@NotNull BaseEntity entity) {
        if (entity.getId() != null || entity.getVersion() != null) {
            throw BaseException.builder()
                    .exceptionEnum(ExceptionEnum.BAD_CREATE_WITH_ID_OR_VERSION)
                    .build();
        }
    }
}
