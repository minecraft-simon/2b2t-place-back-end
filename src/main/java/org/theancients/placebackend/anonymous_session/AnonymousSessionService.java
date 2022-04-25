package org.theancients.placebackend.anonymous_session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnonymousSessionService {

    @Autowired
    private AnonymousSessionRepository anonymousSessionRepository;

    @Scheduled(fixedRate = 5000)
    private void deleteInactiveSessions() {
        List<AnonymousSession> sessions = anonymousSessionRepository.findAll();
        List<AnonymousSession> toDelete = new ArrayList<>();
        Instant now = Instant.now();
        for (AnonymousSession session : sessions) {
            long lastPing = Duration.between(session.getLastPing(), now).getSeconds();
            if (lastPing > 20) { // delete session after 20 seconds of inactivity
                toDelete.add(session);
            }
        }
        anonymousSessionRepository.deleteAll(toDelete);
    }

    public boolean refreshSession(String sessionId) {
        if (sessionId == null || sessionId.length() < 32 || sessionId.length() > 64 || !StringUtils.isAlphanumeric(sessionId)) {
            return false;
        }
        Instant now = Instant.now();
        AnonymousSession anonymousSession = anonymousSessionRepository.findBySessionId(sessionId).orElse(new AnonymousSession(now));
        anonymousSession.setSessionId(sessionId);
        anonymousSession.setLastPing(now);
        anonymousSessionRepository.save(anonymousSession);
        return true;
    }

}
