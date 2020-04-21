package com.kakaopay.preexam.controller;

import com.kakaopay.preexam.exception.BaseException;
import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandlerController {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String, Object> handleAllException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", RESPONSE_STATUS.UNKNOWN.getCode());
        body.put("message", RESPONSE_STATUS.UNKNOWN.getMessage());
        ex.printStackTrace();

        return body;
    }

    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    public Map<String, Object> handleResourceNotFoundException() {
        Map<String, Object> body = new HashMap<>();
        body.put("code", RESPONSE_STATUS.NOT_FOUND.getCode());
        body.put("message", RESPONSE_STATUS.NOT_FOUND.getMessage());

        return body;
    }

    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public Map<String, Object> handleBaseException(BaseException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ex.getErrorCode());
        body.put("message", ex.getMessage());
        log.error(ex.toString());

        return body;
    }
}
