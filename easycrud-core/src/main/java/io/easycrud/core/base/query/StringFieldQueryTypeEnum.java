package io.easycrud.core.base.query;

import io.easycrud.core.base.exception.BaseException;
import io.easycrud.core.base.exception.ExceptionEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StringFieldQueryTypeEnum {

    EQUAL(1);

    private final Integer id;

    public StringFieldQueryTypeEnum fromId(Integer id) {
        return Arrays.stream(StringFieldQueryTypeEnum.values())
                .filter(value -> value.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> BaseException.builder()
                        .exceptionEnum(ExceptionEnum.INVALID_ENUM_ID)
                        .messageParams(new Object[] {id, this.getClass().getName()})
                        .build());
    }
}
