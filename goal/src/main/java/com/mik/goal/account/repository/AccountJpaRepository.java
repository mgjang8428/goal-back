package com.mik.goal.account.repository;

import com.mik.goal.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNickname(String nickname);

    Optional<Account> findByEmail(String email);
}
