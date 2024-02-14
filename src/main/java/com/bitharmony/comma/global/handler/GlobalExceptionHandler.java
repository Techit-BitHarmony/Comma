package com.bitharmony.comma.global.handler;

import com.bitharmony.comma.global.exception.CommaException;
import com.bitharmony.comma.global.response.ErrorResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(CommaException.class)
    public ResponseEntity<ErrorResponse> handlerException(CommaException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(statusCode).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerException(MethodArgumentNotValidException e) {
        int statusCode = e.getStatusCode().value();
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                }); // 일단 보류, 여러개의 binding error가 있을 경우에 사용되는 로직.
                    // 지금 ErrorResponse 객체에 하나의 오류메시지만 담을 수 있게 되어있어서 보류.

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .validMessages(errors)
                .build();

        return ResponseEntity.status(statusCode).body(body);
    }
}
