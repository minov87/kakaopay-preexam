package com.kakaopay.preexam.repository.account;

import com.kakaopay.preexam.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long accountId);

    boolean existsByName(String name);

    Optional<Account> findFirstByName(String name);
}
