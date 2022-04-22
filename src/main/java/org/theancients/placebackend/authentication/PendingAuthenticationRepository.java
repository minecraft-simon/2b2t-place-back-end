package org.theancients.placebackend.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingAuthenticationRepository extends JpaRepository<PendingAuthentication, String> {
    boolean existsByAuthCode(String authCode);
}
