package io.easycrud.core.base.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务异常类
 */
@Getter
public enum ExceptionEnum {

    /**
     * 未找到资源
     */
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND),

    /**
     * 不清楚为何创建失败
     */
    CREATE_FAILED(),

    /**
     * 不清楚为何更新失败
     */
    UPDATE_FAILED(),

    /**
     * 不清楚为何删除失败
     */
    DELETE_FAILED(),

    /**
     * 无效的枚举 id
     */
    INVALID_ENUM_ID(HttpStatus.BAD_REQUEST, "Invalid enum id {0} for {1}"),

    /**
     * 无效的创建请求
     */
    BAD_CREATE_WITH_ID_OR_VERSION(HttpStatus.BAD_REQUEST, "Create operation should not with id or version"),

    /**
     * 无效的更新请求
     */
    BAD_UPDATE_WITHOUT_ID(HttpStatus.BAD_REQUEST, "Update operation should with id"),

    /**
     * 无效的更新请求
     */
    BAD_UPDATE_FOR_NOT_FOUNDED_RECORD(HttpStatus.BAD_REQUEST, "Update operation should for existed record"),

    /**
     * 功能未实现
     */
    NOT_IMPLEMENTATION("This logic is not implemented");

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    private String message;

    ExceptionEnum() {}

    ExceptionEnum(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    ExceptionEnum(String message) {
        this.message = message;
    }

    ExceptionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
