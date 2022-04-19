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

    public StatusResponseDto statusUpdate(StatusRequestDto statusRequestDto) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        List<Point> highlights = highlightService.updateHighlight(statusRequestDto.getUserId(), statusRequestDto.getHighlightPos());
        statusResponseDto.setHighlights(highlights);
        statusResponseDto.setPixelGrid(pixelGridService.getPixelGrid());

        return statusResponseDto;
    }

}
