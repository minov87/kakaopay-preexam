package com.kakaopay.preexam.service.account;

import com.kakaopay.preexam.dto.account.AccountDto;
import com.kakaopay.preexam.exception.AccountException;
import com.kakaopay.preexam.model.account.Account;
import com.kakaopay.preexam.model.token.Token;
import com.kakaopay.preexam.repository.account.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

@DisplayName("계정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    AccountException accountException;

    @Mock
    Token token;

    // 로그인 mockup 데이터 생성
    private static final AccountDto accountDto = AccountDto.builder()
            .name("test")
            .password("test")
            .build();

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    @DisplayName("회원가입 테스트")
    public void testSignUp() throws Exception {
        token = accountService.signUp(accountDto);
        verify(accountRepository, atLeastOnce()).save(any());
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    @Test
    @DisplayName("존재하는 사용자 정보로 회원 가입 시도시 에러 테스트")
    public void testSignUpAccountAlreadyExistException() {
        when(accountRepository.existsByName(accountDto.getName())).thenReturn(true);

        accountException = assertThrows(AccountException.class, () ->
                accountService.signUp(accountDto));
        assertEquals("Account already exist", accountException.getMessage());
        assertEquals(209, accountException.getErrorCode());
    }

    @Test
    @DisplayName("회원 로그인 테스트")
    public void testSignIn() throws Exception {
        when(accountRepository.findFirstByName(accountDto.getName())).thenReturn(
                Optional.of(Account.builder()
                        .id(1L)
                        .name(accountDto.getName())
                        .password(passwordEncoder.encode(accountDto.getPassword()))
                        .build()));
        token = accountService.signIn(accountDto);
        verify(accountRepository, atLeastOnce()).findFirstByName(accountDto.getName());
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 정보로 회원 로그인 시도시 에러 테스트")
    public void testSignInAccountNotExistException() {
        accountException = assertThrows(AccountException.class, () ->
                accountService.signIn(accountDto));
        assertEquals("Account not exist", accountException.getMessage());
        assertEquals(210, accountException.getErrorCode());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 테스트 시도시 에러 테스트")
    public void testSignInAccountWrongPasswordException() {
        when(accountRepository.findFirstByName(accountDto.getName())).thenReturn(
                Optional.of(Account.builder()
                        .id(1L)
                        .name(accountDto.getName())
                        .password(passwordEncoder.encode(accountDto.getPassword()).concat("_ErrorPwd"))
                        .build()));
        accountException = assertThrows(AccountException.class, () ->
                accountService.signIn(accountDto));
        assertEquals("Account password is wrong", accountException.getMessage());
        assertEquals(211, accountException.getErrorCode());
    }
}