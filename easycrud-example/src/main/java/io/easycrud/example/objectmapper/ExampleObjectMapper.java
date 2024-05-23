package io.easycrud.example.objectmapper;

import io.easycrud.core.base.objectmapper.BaseObjectMapper;
import io.easycrud.example.pojo.ExampleDO;
import io.easycrud.example.pojo.ExampleDTO;
import io.easycrud.example.pojo.ExamplePO;
import io.easycrud.example.pojo.ExampleVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExampleObjectMapper extends BaseObjectMapper<ExampleDTO, ExampleVO, ExampleDO, ExamplePO> {

}
