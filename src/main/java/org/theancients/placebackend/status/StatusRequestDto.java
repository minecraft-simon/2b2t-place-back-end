package org.theancients.placebackend.status;

import java.awt.*;

public class StatusRequestDto {

    private String sessionId;
    private Point highlightPos;
    private boolean requestAuthToken;

    @Override
    public String toString() {
        return "StatusRequestDto{" +
                "sessionId='" + sessionId + '\'' +
                ", highlightPos=" + highlightPos +
                ", requestAuthToken=" + requestAuthToken +
                '}';
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Point getHighlightPos() {
        return highlightPos;
    }

    public void setHighlightPos(Point highlightPos) {
        this.highlightPos = highlightPos;
    }

    public boolean isRequestAuthToken() {
        return requestAuthToken;
    }

    public void setRequestAuthToken(boolean requestAuthToken) {
        this.requestAuthToken = requestAuthToken;
    }

}
