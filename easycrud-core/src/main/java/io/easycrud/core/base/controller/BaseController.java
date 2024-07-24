package com.bosch.cn.em.mfd.core.base.controller;

import io.easycrud.core.base.entity.BaseDTO;
import io.easycrud.core.base.entity.BaseVO;
import io.easycrud.core.base.exception.BaseException;
import io.easycrud.core.base.exception.ExceptionEnum;
import io.easycrud.core.base.service.BaseService;
import io.easycrud.core.constant.URLConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
public class BaseController<DTO extends BaseDTO, VO extends BaseVO> {

    @Autowired
    protected BaseService<DTO, VO> baseService;

    //    @GetMapping
    @Operation(summary = "find all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> find() {
        return ResponseEntity.ok(baseService.find());
    }

    //    @GetMapping
    @Operation(summary = "find one page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> find(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(baseService.find(pageable));
    }

    //    @PostMapping
    @Operation(summary = "create one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create Success"),
            @ApiResponse(responseCode = "400", description = "Valid Request Body Failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> create(@RequestBody DTO dto) {

        VO vo = baseService.create(dto)
                .orElseThrow(() -> BaseException.builder()
                        .exceptionEnum(ExceptionEnum.CREATE_FAILED)
                        .build());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(URLConstants.PATH_VARIABLE_ID)
                .buildAndExpand(vo.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    //    @GetMapping(URLConstants.PATH_VARIABLE_ID)
    @Operation(summary = "find by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> find(@PathVariable String id) {

        VO vo = baseService.find(id)
                .orElseThrow(() -> BaseException.builder()
                        .exceptionEnum(ExceptionEnum.NOT_FOUND_EXCEPTION)
                        .build());

        return ResponseEntity.ok(vo);
    }

    //    @PutMapping(URLConstants.PATH_VARIABLE_ID)
    @Operation(summary = "update by id", description = "including null fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Success"),
            @ApiResponse(responseCode = "400", description = "Valid Request Body Failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> updateAllFields(@PathVariable String id, @RequestBody DTO dto) {

        dto.setId(id);
        baseService.update(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(URLConstants.PATH_VARIABLE_ID)
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.ok(uri);
    }

    //    @PatchMapping(URLConstants.PATH_VARIABLE_ID)
    @Operation(summary = "update by id", description = "excluding null fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Success"),
            @ApiResponse(responseCode = "400", description = "Valid Request Body Failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> updateSelectiveFields(@PathVariable String id, @RequestBody DTO dto) {

        dto.setId(id);
        baseService.update(dto, true);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(URLConstants.PATH_VARIABLE_ID)
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.ok(uri);
    }

    //    @DeleteMapping(URLConstants.PATH_VARIABLE_ID)
    @Operation(summary = "delete by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> delete(@PathVariable String id) {
        baseService.remove(id);
        return ResponseEntity.noContent().build();
    }

}
