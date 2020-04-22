package com.kakaopay.preexam.controller.account;

import com.kakaopay.preexam.anotation.NoneAuth;
import com.kakaopay.preexam.dto.account.AccountDto;
import com.kakaopay.preexam.exception.BaseException;
import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import com.kakaopay.preexam.model.response.Response;
import com.kakaopay.preexam.model.token.Token;
import com.kakaopay.preexam.service.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    /**
     * 회원가입
     *
     * @param param 가입 계정명, 가입 비밀번호
     * @return
     */
    @NoneAuth
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response signUp(
            @RequestBody(required = true) AccountDto param) throws Exception {
        if(param.getName() == null || param.getPassword() == null) {
            throw new BaseException(RESPONSE_STATUS.BAD_REQUEST.getMessage(), RESPONSE_STATUS.BAD_REQUEST.getCode());
        }

        Token token = accountService.signUp(param);
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).data(token).build();
    }

    /**
     * 로그인
     *
     * @param param 로그인 계정명, 로그인 비밀번호
     * @return
     */
    @NoneAuth
    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response signIn(
            @RequestBody(required = true) AccountDto param) throws Exception {
        if(param.getName() == null || param.getPassword() == null) {
            throw new BaseException(RESPONSE_STATUS.BAD_REQUEST.getMessage(), RESPONSE_STATUS.BAD_REQUEST.getCode());
        }

        Token token = accountService.signIn(param);
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).data(token).build();
    }
}
