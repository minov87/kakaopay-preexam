package com.kakaopay.preexam.service.token;

import com.kakaopay.preexam.exception.TokenException;
import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import com.kakaopay.preexam.model.token.Token;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class TokenService {
    private final String key = "kAkAo20PaY20_s$CuRiTyKeY!";
    private final String issuer = "KakaoPay";
    private final int accessTokenTime = 600; // 10분
    private final int refreshTokenTime = 3600; // 1시간

    /**
     * 토큰 발급
     *
     * @param body
     * @return
     */
    public Token createToken(Map<String, Object> body) {
        String accessToken = Jwts.builder()
                .setIssuer(issuer)
                .setHeaderParam("typ", "JWT")
                .setClaims(body)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * accessTokenTime))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuer(issuer)
                .setHeaderParam("typ", "JWT")
                .setClaims(body)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * refreshTokenTime))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return token;
    }

    /**
     * 토큰 검증
     *
     * @param token 토큰
     * @return
     * @throws Exception
     */
    public boolean verifyToken(String token) throws Exception {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(key.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenException(RESPONSE_STATUS.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new TokenException(RESPONSE_STATUS.TOKEN_VERIFY_FAIL);
        }
    }
}
