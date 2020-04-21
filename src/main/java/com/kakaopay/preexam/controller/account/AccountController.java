package com.kakaopay.preexam.controller.account;

import com.kakaopay.preexam.model.account.AccountParams;
import com.kakaopay.preexam.model.response.Response;
import com.kakaopay.preexam.model.token.Token;
import com.kakaopay.preexam.service.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
     * 회원 가입
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response signUp(
            @RequestBody(required = true) AccountParams param) {
        if(param.getName() == null || param.getPassword() == null) {
            return Response.builder().code(HttpStatus.BAD_REQUEST.value()).msg("invalid parameter").build();
        }

        try {
            Token token = accountService.signUp(param);
            return Response.builder().code(HttpStatus.OK.value()).data(token).build();
        } catch (Exception e) {
            log.error(e.toString());
            return Response.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(e.getMessage()).build();
        }
    }

    // 회원 로그인
    //public Response signIn() {
    //
    //}
}
