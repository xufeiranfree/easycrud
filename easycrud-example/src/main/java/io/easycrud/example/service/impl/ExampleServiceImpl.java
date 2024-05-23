package io.easycrud.example.service.impl;

import io.easycrud.core.base.service.impl.BaseServiceImpl;
import io.easycrud.example.pojo.ExampleDO;
import io.easycrud.example.pojo.ExampleDTO;
import io.easycrud.example.pojo.ExamplePO;
import io.easycrud.example.pojo.ExampleVO;
import io.easycrud.example.service.ExampleService;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl extends BaseServiceImpl<ExampleDTO, ExampleVO, ExampleDO, ExamplePO> implements ExampleService {
}
