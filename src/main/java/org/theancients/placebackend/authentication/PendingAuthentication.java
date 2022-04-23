package org.theancients.placebackend.authentication;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
public class PendingAuthentication {

    @Id
    private String sessionId;
    private String authCode;
    private Instant expiry;
    private String identity;

    public PendingAuthentication() {

    }

    public PendingAuthentication(String sessionId, String authCode) {
        this.sessionId = sessionId;
        this.authCode = authCode;
        expiry = Instant.now().plus(10, ChronoUnit.MINUTES);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

}
