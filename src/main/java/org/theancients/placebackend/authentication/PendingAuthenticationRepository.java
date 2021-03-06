package org.theancients.placebackend.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingAuthenticationRepository extends JpaRepository<PendingAuthentication, String> {
    boolean existsBySessionId(String sessionId);
    boolean existsByAuthCode(String authCode);
    Optional<PendingAuthentication> findBySessionId(String sessionId);
    Optional<PendingAuthentication> findByAuthCode(String authCode);
    void deleteBySessionId(String sessionId);
}
