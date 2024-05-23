package io.easycrud.example;

import io.easycrud.core.base.controller.BaseController;
import io.easycrud.core.constant.URLConstants;
import io.easycrud.example.pojo.ExampleDTO;
import io.easycrud.example.pojo.ExampleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(URLConstants.PUB_V1 + "/example")
public class ExamplePubApiV1Controller extends BaseController<ExampleDTO, ExampleVO> {

}
