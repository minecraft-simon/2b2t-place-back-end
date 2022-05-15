package org.theancients.placebackend.last_pixel_placer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class LastPixelPlacer {

    @Id
    private int id;
    private int x;
    private int y;
    private byte color;
    private long player;

    @Override
    public String toString() {
        return "LastPixelPlacer{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", color=" + color +
                ", player=" + player +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public long getPlayer() {
        return player;
    }

    public void setPlayer(long player) {
        this.player = player;
    }

}
