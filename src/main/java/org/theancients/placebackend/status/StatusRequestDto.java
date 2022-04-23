package org.theancients.placebackend.status;

import java.awt.*;

public class StatusRequestDto {

    private String sessionId;
    private Point highlightPos;

    @Override
    public String toString() {
        return "StatusRequestDto{" +
                "sessionId='" + sessionId + '\'' +
                ", highlightPos=" + highlightPos +
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

}
