package io.easycrud.example;


import io.easycrud.core.constant.URLConstants;
import io.easycrud.example.pojo.ExampleDTO;
import io.easycrud.example.pojo.ExampleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(URLConstants.OA2_V1 + "/example")
public class ExamplePubApiV1Controller extends com.bosch.cn.em.mfd.core.base.controller.BaseController<ExampleDTO, ExampleVO> {

    @Override
    @GetMapping
    public ResponseEntity<?> find() {
        return super.find();
    }

    @GetMapping("/test")
    public String test() {
        return "v1";
    }
}
