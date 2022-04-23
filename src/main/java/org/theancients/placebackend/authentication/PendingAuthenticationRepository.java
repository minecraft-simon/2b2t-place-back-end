package org.theancients.placebackend.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingAuthenticationRepository extends JpaRepository<PendingAuthentication, String> {
    boolean existsByAuthCode(String authCode);
    Optional<PendingAuthentication> findByAuthCode(String authCode);
}
