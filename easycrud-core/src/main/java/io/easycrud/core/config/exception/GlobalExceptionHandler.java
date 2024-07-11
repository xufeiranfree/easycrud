package io.easycrud.core.config.exception;

import io.easycrud.core.base.exception.BaseException;
import io.easycrud.core.base.exception.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException exception) {
        ExceptionEnum exceptionEnum = exception.getExceptionEnum();
        String body = Optional.ofNullable(exceptionEnum.getMessage())
                .map(msg -> String.format(msg, exception.getMessageParams()))
                .orElse(null);
        return ResponseEntity.status(exceptionEnum.getHttpStatus())
                .body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Object[] arguments = error.getArguments();
            if (arguments != null && arguments.length > 1) {
                Object[] messageArguments = new Object[arguments.length - 1];
                System.arraycopy(arguments, 1, messageArguments, 0, arguments.length - 1);
                errorMessage = formatMessage(errorMessage, messageArguments);
            }
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    private String formatMessage(String message, Object[] arguments) {
        return MessageFormat.format(message, arguments);
    }
}
