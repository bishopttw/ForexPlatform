package com.bishop.forexplatform.repository;

import com.bishop.forexplatform.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository <VerificationToken, Long>{
    Optional<VerificationToken> findByToken(String token);
}
