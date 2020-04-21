package com.kakaopay.preexam.service.account;

import com.kakaopay.preexam.model.account.Account;
import com.kakaopay.preexam.model.account.AccountParams;
import com.kakaopay.preexam.model.token.Token;
import com.kakaopay.preexam.repository.account.AccountRepository;
import com.kakaopay.preexam.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            TokenService tokenService,
            AccountRepository accountRepository) {
        this.tokenService = tokenService;
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
    public Token signUp(AccountParams params) throws Exception {
        if(accountRepository.existsByName(params.getName())) {
            throw new Exception("fail to signup - already signup.");
        }

        Account account = Account.builder()
                .name(params.getName())
                .password(passwordEncoder.encode(params.getPassword()))
                .build();
        Long accountId = accountRepository.save(account).getId();

        return getToken(accountId);
    }

    /**
     * 발급된 토큰 정보 가져오기

     * @param accountId
     * @return
     */
    private Token getToken(Long accountId) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", accountId);

        return tokenService.createToken(bodyMap);
    }
}
