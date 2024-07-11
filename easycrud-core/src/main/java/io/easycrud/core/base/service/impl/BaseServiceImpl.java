package io.easycrud.core.base.service.impl;

import io.easycrud.core.base.entity.BaseDO;
import io.easycrud.core.base.entity.BaseDTO;
import io.easycrud.core.base.entity.BasePO;
import io.easycrud.core.base.entity.BaseVO;
import io.easycrud.core.base.objectmapper.BaseObjectMapper;
import io.easycrud.core.base.repository.BaseRepository;
import io.easycrud.core.base.service.BaseService;
import io.easycrud.core.config.header.CommonHeaderManager;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BaseServiceImpl<DTO extends BaseDTO, VO extends BaseVO, DO extends BaseDO, PO extends BasePO> implements BaseService<DTO, VO> {

    @Autowired
    protected BaseRepository<DO> baseRepository;

    @Autowired
    protected BaseObjectMapper<DTO, VO, DO, PO> baseObjectMapper;

    @Override
    public Collection<VO> find() {
        List<DO> dos = baseRepository.findAll(CommonHeaderManager.getCommonHeaders().getUserId());
        return baseObjectMapper.dosToVOs(dos);
    }

    @Override
    public Page<VO> find(Pageable pageable) {
        Page<DO> doPage = baseRepository.findAll(pageable, CommonHeaderManager.getCommonHeaders().getUserId());
        List<VO> vos = Optional.of(doPage)
                .map(Page::getContent)
                .map(baseObjectMapper::dosToVOs)
                .orElse(new ArrayList<>());
        return new PageImpl<>(vos, pageable, doPage.getTotalElements());
    }

    @Override
    public Optional<VO> find(@NotBlank String id) {
        Optional<DO> doOpt = baseRepository.find(id, CommonHeaderManager.getCommonHeaders().getUserId());
        return doOpt.map(baseObjectMapper::doToVO);
    }

    @Override
    public Optional<VO> create(@NotNull DTO dto) {
        DO aDo = baseObjectMapper.dtoToDO(dto);
        DO saved = baseRepository.create(aDo);
        VO vo = baseObjectMapper.doToVO(saved);
        return Optional.ofNullable(vo);
    }

    @Override
    @Transactional
    public void update(@NotNull DTO dto) {
        update(dto, false);
    }

    @Override
    @Transactional
    public void update(@NotNull DTO dto, boolean selective) {

        DO aDo = baseObjectMapper.dtoToDO(dto);

        if (selective) {
            baseRepository.updateSelective(aDo);
        } else {
            baseRepository.update(aDo);
        }
    }

    @Override
    public void remove(@NotBlank String id) {
        baseRepository.delete(id, CommonHeaderManager.getCommonHeaders().getUserId());
    }
}
