package com.kakaopay.preexam.interceptor;

import com.kakaopay.preexam.anotation.NoneAuth;
import com.kakaopay.preexam.exception.TokenException;
import com.kakaopay.preexam.repository.account.AccountRepository;
import com.kakaopay.preexam.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    TokenService tokenService;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        NoneAuth noneAuth = ((HandlerMethod) handler).getMethodAnnotation(NoneAuth.class);
        if (noneAuth != null) {
            // 토큰 검증하지 않음.
            return true;
        } else {
            // 토큰 검증.
            if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
                String[] bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ");
                String token = bearerToken[0].equalsIgnoreCase("Bearer") ? bearerToken[1] : null;
                HashMap<String, Object> verifyTokenResultMap = tokenService.verifyToken(token);

                // 토큰 유무 토큰 검증 결과 유무 체크
                if (token != null && !verifyTokenResultMap.isEmpty()
                        && verifyTokenResultMap.get("result") != null
                        && verifyTokenResultMap.get("accountId") != null
                        && (Boolean) verifyTokenResultMap.get("result")) {
                    // 존재하는 사용자인지 검증
                    Long accountId = Long.parseLong(verifyTokenResultMap.get("accountId").toString());
                    if (accountRepository.existsById(accountId)) {
                        return true;
                    } else {
                        throw TokenException.TOKEN_VERIFY_FAIL_ACCOUNT_NOT_EXIST;
                    }
                } else {
                    throw TokenException.TOKEN_VERIFY_FAIL;
                }
            } else {
                throw TokenException.TOKEN_EMPTY;
            }
        }
    }
}
