package io.easycrud.core.base.service;

import java.util.Collection;
import java.util.Optional;

public interface BaseService<DTO, VO> {

    /**
     * 列出所有
     * @return
     */
    Collection<VO> find();

    /**
     * 新建一个
     * @param dto
     * @return
     */
    Optional<VO> create(DTO dto);

    /**
     * 获取某个
     * @param id
     * @return
     */
    Optional<VO> find(String id);

    /**
     * 更新某一个
     * @param id
     * @param dto
     * @param selective true: 选择性更新，只更新不为 null 的属性
     * @return
     */
    Optional<VO> update(String id, DTO dto, boolean selective);

    /**
     * 删除某一个
     * @param id
     */
    void remove(String id);
}
