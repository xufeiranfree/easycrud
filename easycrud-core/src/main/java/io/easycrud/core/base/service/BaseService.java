package io.easycrud.core.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

public interface BaseService<DTO, VO> {

    /**
     * 列出所有
     * @return
     */
    Collection<VO> find();

    Page<VO> find(Pageable pageable);

    /**
     * 获取某个
     * @param id
     * @return
     */
    Optional<VO> find(String id);

    /**
     * 新建一个
     * @param dto
     * @return
     */
    Optional<VO> create(DTO dto);

    @Transactional
    void update(DTO dto);

    /**
     * 更新某一个
     * @param dto
     * @param selective true: 选择性更新，只更新不为 null 的属性
     * @return
     */
    void update(DTO dto, boolean selective);

    /**
     * 删除某一个
     * @param id
     */
    void remove(String id);
}
