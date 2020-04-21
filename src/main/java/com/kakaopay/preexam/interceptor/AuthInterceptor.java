package com.kakaopay.preexam.interceptor;

import com.kakaopay.preexam.anotation.NoneAuth;
import com.kakaopay.preexam.exception.TokenException;
import com.kakaopay.preexam.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        NoneAuth noneAuth = ((HandlerMethod) handler).getMethodAnnotation(NoneAuth.class);
        if(noneAuth != null) {
            // 토큰 검증하지 않음.
            return true;
        } else {
            // 토큰 검증.
            if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
                String[] bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ");
                String token = bearerToken[0].equalsIgnoreCase("Bearer") ? bearerToken[1] : null;
                if (token != null && tokenService.verifyToken(token)) {
                    return true;
                } else {
                    throw TokenException.TOKEN_VERIFY_FAIL;
                }
            } else {
                throw TokenException.TOKEN_EMPTY;
            }
        }
    }
}
