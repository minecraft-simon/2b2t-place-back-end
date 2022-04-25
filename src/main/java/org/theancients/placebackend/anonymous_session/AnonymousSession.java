package org.theancients.placebackend.anonymous_session;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class AnonymousSession {

    @Id
    private String sessionId;
    private Instant lastPing;
    private Instant created;

    public AnonymousSession() {
    }

    public AnonymousSession(Instant created) {
        this.created = created;
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

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

}
