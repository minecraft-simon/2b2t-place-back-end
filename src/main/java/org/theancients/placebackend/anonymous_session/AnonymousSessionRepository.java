package org.theancients.placebackend.anonymous_session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnonymousSessionRepository extends JpaRepository<AnonymousSession, String> {
    Optional<AnonymousSession> findBySessionId(String sessionId);
}
