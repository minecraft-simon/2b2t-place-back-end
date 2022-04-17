package org.theancients.placebackend.highlight;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Highlight {

    @Id
    private String id;
    private int x;
    private int y;
    private Instant lastPing;
    private Instant lastChange;

    public Highlight() {
    }

    public Highlight(String id) {
        this(id, -1, -1, Instant.now(), Instant.now());
    }

    public Highlight(String id, int x, int y, Instant lastPing, Instant lastChange) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.lastPing = lastPing;
        this.lastChange = lastChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Highlight highlight = (Highlight) o;
        return Objects.equals(id, highlight.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Highlight{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", lastPing=" + lastPing +
                ", lastChange=" + lastChange +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

    public Instant getLastChange() {
        return lastChange;
    }

    public void setLastChange(Instant lastChange) {
        this.lastChange = lastChange;
    }

}
