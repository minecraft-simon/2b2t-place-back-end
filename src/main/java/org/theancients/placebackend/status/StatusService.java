package org.theancients.placebackend.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.highlight.HighlightService;
import org.theancients.placebackend.pixel_grid.PixelGridService;

import java.awt.*;
import java.util.List;

@Service
public class StatusService {

    @Autowired
    private HighlightService highlightService;

    @Autowired
    private PixelGridService pixelGridService;

    public StatusOutDto statusUpdate(StatusInDto statusInDto) {
        StatusOutDto statusOutDto = new StatusOutDto();
        List<Point> highlights = highlightService.updateHighlight(statusInDto.getUserId(), statusInDto.getHighlightPos());
        statusOutDto.setHighlights(highlights);
        statusOutDto.setPixelGrid(pixelGridService.getPixelGrid());

        return statusOutDto;
    }

}
