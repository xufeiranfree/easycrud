package io.easycrud.core.base.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    private Object[] messageParams;

}
