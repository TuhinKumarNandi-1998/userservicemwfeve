package com.scaler.userservicemwfeve.Repositories;

import com.scaler.userservicemwfeve.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    Optional<Token> findByValueAndDeletedEquals(String value, boolean deleted);

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThanEqual(String value, boolean deleted, Date date);
}
