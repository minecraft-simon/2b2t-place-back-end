package org.theancients.placebackend.status;

import org.theancients.placebackend.pixel_grid.PixelGrid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StatusOutDto {

    private List<Point> highlights = new ArrayList<>();
    private PixelGrid pixelGrid;
    private int pollingDelay = 500;

    public List<Point> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<Point> highlights) {
        this.highlights = highlights;
    }

    public PixelGrid getPixelGrid() {
        return pixelGrid;
    }

    public void setPixelGrid(PixelGrid pixelGrid) {
        this.pixelGrid = pixelGrid;
    }

    public int getPollingDelay() {
        return pollingDelay;
    }

    public void setPollingDelay(int pollingDelay) {
        this.pollingDelay = pollingDelay;
    }
}
