package com.kakaopay.preexam.service.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.preexam.exception.TokenException;
import com.kakaopay.preexam.model.token.Token;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("토큰 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    private static final String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOjEsImV4cCI6MTU4NzUxODcwNX0.zWPiAzm1sJB5g8OnjCmVxJTxpgb6i3fNeg7zaeH5Ytk"; // 만료일 4/22
    private static final String useAbleToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOjEsImV4cCI6MTU4ODkwMDUwNX0.04AcNFUDigCDcMAF-X_Zps501fhrz2tpXB-NqHNOHfI"; // 만료일 5/8

    @InjectMocks
    TokenService tokenService;

    @Mock
    TokenException tokenException;

    @Test
    @DisplayName("토큰 생성 테스트")
    public void testCreateToken() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("testId", 1);
        Token token = tokenService.createToken(testMap);

        // 데이터 검증을 위해 Map 형태로 변환
        ObjectMapper mapObject = new ObjectMapper();
        Map<String, Object> resultMap = mapObject.convertValue(token, Map.class);

        // AccessToken & RefreshToken 체크
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
        assertThat(resultMap, IsMapContaining.hasKey("accessToken"));
    }

    @Test
    @DisplayName("토큰 검증 테스트")
    public void testVerifyToken() throws Exception {
        // 토큰 값이 Null 일 경우 에러 확인
        tokenException = assertThrows(TokenException.class, () ->
                tokenService.verifyToken(null));
        assertEquals("Token Empty", tokenException.getMessage());
        assertEquals(208, tokenException.getErrorCode());

        // 만료 토큰 검증 및 토큰 에러 확인
        tokenException = assertThrows(TokenException.class, () ->
                tokenService.verifyToken(expiredToken));
        assertEquals("Token expired", tokenException.getMessage());
        assertEquals(207, tokenException.getErrorCode());

        // 데이터 변조 토큰 검증 및 토큰 에러 확인
        tokenException = assertThrows(TokenException.class, () ->
                tokenService.verifyToken(useAbleToken.concat("__Modify")));
        assertEquals("Token verify fail", tokenException.getMessage());
        assertEquals(206, tokenException.getErrorCode(), "");

        assertTrue(tokenService.verifyToken(useAbleToken));
    }
}
