package com.kakaopay.preexam.service.account;

import com.kakaopay.preexam.dto.account.AccountDto;
import com.kakaopay.preexam.exception.AccountException;
import com.kakaopay.preexam.model.account.Account;
import com.kakaopay.preexam.model.token.Token;
import com.kakaopay.preexam.repository.account.AccountRepository;
import com.kakaopay.preexam.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AccountService {
    private final TokenService tokenService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(
            AccountRepository accountRepository) {
        this.tokenService = new TokenService();
        this.accountRepository = accountRepository;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 회원가입
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Transactional
    public Token signUp(AccountDto params) throws Exception {
        // 기 가입된 사용자 인지 확인
        if(accountRepository.existsByName(params.getName())) {
            throw AccountException.ACCOUNT_ALREADY_EXIST;
        }

        // 화원가입 진행
        Account account = Account.builder()
                .name(params.getName())
                .password(passwordEncoder.encode(params.getPassword()))
                .build();

        return getToken(accountRepository.save(account).getId());
    }

    /**
     * 로그인
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Token signIn(AccountDto params) throws Exception {
        // 사용자 계정 확인
        Account account = accountRepository.findFirstByName(params.getName())
                .orElseThrow(() -> AccountException.ACCOUNT_NOT_EXIST);

        // 비밀번호 검증
        if(passwordEncoder.matches(params.getPassword(), account.getPassword())) {
            return getToken(account.getId());
        } else {
            throw AccountException.ACCOUNT_WRONG_PASSWORD;
        }
    }

    /**
     * 발급된 토큰 정보 가져오기

     * @param id
     * @return
     */
    private Token getToken(Long id) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("accountName", id);

        return tokenService.createToken(bodyMap);
    }
}
