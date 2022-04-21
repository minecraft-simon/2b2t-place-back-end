package org.theancients.placebackend.anonymous_session;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class AnonymousSession {

    @Id
    private String sessionId;
    private Instant lastPing;

    public AnonymousSession() {
    }

    public AnonymousSession(String sessionId) {
        this.sessionId = sessionId;
        lastPing = Instant.now();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String id) {
        this.sessionId = id;
    }

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

}
