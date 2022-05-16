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
    private String remoteAddress;

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

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

}
