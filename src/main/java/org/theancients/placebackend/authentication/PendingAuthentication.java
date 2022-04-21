package org.theancients.placebackend.authentication;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PendingAuthentication {

    @Id
    private String authCode;
    private String sessionId;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
