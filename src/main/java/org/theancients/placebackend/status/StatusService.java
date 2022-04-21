package org.theancients.placebackend.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.anonymous_session.AnonymousSessionService;
import org.theancients.placebackend.highlight.HighlightService;
import org.theancients.placebackend.pixel_grid.PixelGridService;

import java.awt.*;
import java.util.List;

@Service
public class StatusService {

    @Autowired
    private AnonymousSessionService anonymousSessionService;

    @Autowired
    private HighlightService highlightService;

    @Autowired
    private PixelGridService pixelGridService;

    public StatusResponseDto statusUpdate(StatusRequestDto statusRequestDto) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();

        boolean sessionValid = anonymousSessionService.refreshSession(statusRequestDto.getSessionId());
        if (sessionValid) {
            List<Point> highlights = highlightService.updateHighlight(statusRequestDto.getSessionId(), statusRequestDto.getHighlightPos());
            statusResponseDto.setHighlights(highlights);
            statusResponseDto.setPixelGrid(pixelGridService.getPixelGrid());
        }

        return statusResponseDto;
    }

}
