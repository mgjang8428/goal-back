package com.mik.goal.account.repository;

import com.mik.goal.account.entity.AccountAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountAuthJpaRepository extends JpaRepository<AccountAuth, Long> {

    Optional<AccountAuth> findByUsername(String username);

    Optional<AccountAuth> findByAccountAccessId(UUID accountAccessId);
}
