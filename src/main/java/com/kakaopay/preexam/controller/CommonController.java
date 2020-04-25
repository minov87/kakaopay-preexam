package com.kakaopay.preexam.controller;

import com.kakaopay.preexam.anotation.NoneAuth;
import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import com.kakaopay.preexam.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/")
public class CommonController {
    @NoneAuth
    @RequestMapping(value="/" , method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response HelloApi() {
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).data("Welcome to KakaoPay Pre-exam API").build();
    }
}
