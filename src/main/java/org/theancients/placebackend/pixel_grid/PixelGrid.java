package org.theancients.placebackend.pixel_grid;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class PixelGrid {

    @Id
    private long id;
    @Column(length = 64000)
    private byte[] pixels;
    private String comment;

    @Override
    public String toString() {
        return "PixelGrid{" +
                "id=" + id +
                ", pixels=" + Arrays.toString(pixels) +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getPixels() {
        return pixels;
    }

    public void setPixels(byte[] pixels) {
        this.pixels = pixels;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
