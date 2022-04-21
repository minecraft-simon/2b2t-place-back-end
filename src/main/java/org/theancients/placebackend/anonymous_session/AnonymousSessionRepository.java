package org.theancients.placebackend.anonymous_session;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonymousSessionRepository extends JpaRepository<AnonymousSession, String> {
}
