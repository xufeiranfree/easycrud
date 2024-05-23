package io.easycrud.core.base.repository;

import io.easycrud.core.base.entity.BaseDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<DO extends BaseDO> extends JpaRepository<DO, String> {

}
