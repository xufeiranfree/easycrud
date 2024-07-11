package io.easycrud.example.repository.impl;


import io.easycrud.core.base.repository.BaseRepository;
import io.easycrud.example.pojo.ExampleDO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ExampleRepositoryImpl extends BaseRepository<ExampleDO> {

    @Override
    protected Class<ExampleDO> getDomainClass() {
        return ExampleDO.class;
    }
}
