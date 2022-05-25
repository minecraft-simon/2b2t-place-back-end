package org.theancients.placebackend.recorded_pixel;

import java.util.Objects;

public class ScoreDto implements Comparable<ScoreDto> {

    private String playerName;
    private int score;

    @Override
    public int compareTo(ScoreDto o) {
        return Integer.compare(score, o.score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreDto scoreDto = (ScoreDto) o;
        return score == scoreDto.score && Objects.equals(playerName, scoreDto.playerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, score);
    }

    @Override
    public String toString() {
        return "ScoreDto{" +
                "playerName='" + playerName + '\'' +
                ", score=" + score +
                '}';
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
