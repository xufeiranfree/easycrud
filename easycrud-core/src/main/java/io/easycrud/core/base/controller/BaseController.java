package io.easycrud.core.base.controller;


import io.easycrud.core.base.entity.BaseDTO;
import io.easycrud.core.base.entity.BaseEntityAudit;
import io.easycrud.core.base.entity.BaseVO;
import io.easycrud.core.base.exception.BaseException;
import io.easycrud.core.base.exception.ExceptionEnum;
import io.easycrud.core.base.service.BaseService;
import io.easycrud.core.constant.URLConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public class BaseController<DTO extends BaseDTO, VO extends BaseVO> {

    @Autowired
    protected BaseService<DTO, VO> baseService;

    @GetMapping
    public ResponseEntity<?> find() {

        Collection<VO> all = baseService.find();
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DTO dto) {

        Optional<VO> vo = baseService.create(dto);

        vo.orElseThrow(() -> BaseException.builder()
                .exceptionEnum(ExceptionEnum.CREATE_FAILED)
                .build());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(URLConstants.PATH_VARIABLE_ID)
                .buildAndExpand(vo.map(BaseEntityAudit::getId).orElse(null))
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(URLConstants.PATH_VARIABLE_ID)
    public ResponseEntity<?> find(@PathVariable String id) {

        Optional<VO> vo = baseService.find(id);

        vo.orElseThrow(() -> BaseException.builder()
                .exceptionEnum(ExceptionEnum.NOT_FOUND_EXCEPTION)
                .build());

        return ResponseEntity.ok(vo.get());
    }

    @PutMapping(URLConstants.PATH_VARIABLE_ID)
    public ResponseEntity<?> updateAllFields(@PathVariable String id, @RequestBody DTO dto) {

        Optional<VO> vo = baseService.update(id, dto, false);

        vo.orElseThrow(() -> BaseException.builder()
                .exceptionEnum(ExceptionEnum.UPDATE_FAILED)
                .build());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(URLConstants.PATH_VARIABLE_ID)
                .buildAndExpand(vo.map(BaseEntityAudit::getId).orElse(null))
                .toUri();
        return ResponseEntity.ok(uri);
    }

    @PatchMapping(URLConstants.PATH_VARIABLE_ID)
    public ResponseEntity<?> updateSelectiveFields(@PathVariable String id, @RequestBody DTO dto) {

        Optional<VO> vo = baseService.update(id, dto, true);

        vo.orElseThrow(() -> BaseException.builder()
                .exceptionEnum(ExceptionEnum.UPDATE_FAILED)
                .build());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(URLConstants.PATH_VARIABLE_ID)
                .buildAndExpand(vo.map(BaseEntityAudit::getId).orElse(null))
                .toUri();
        return ResponseEntity.ok(uri);
    }

    @DeleteMapping(URLConstants.PATH_VARIABLE_ID)
    public ResponseEntity<?> delete(@PathVariable String id) {
        baseService.remove(id);
        return ResponseEntity.noContent().build();
    }

}
