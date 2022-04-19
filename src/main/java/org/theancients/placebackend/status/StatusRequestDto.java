package org.theancients.placebackend.status;

import java.awt.*;

public class StatusRequestDto {

    private String userId;
    private Point highlightPos;

    @Override
    public String toString() {
        return "StatusInDto{" +
                "userId='" + userId + '\'' +
                ", highlightPos=" + highlightPos +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Point getHighlightPos() {
        return highlightPos;
    }

    public void setHighlightPos(Point highlightPos) {
        this.highlightPos = highlightPos;
    }
}
