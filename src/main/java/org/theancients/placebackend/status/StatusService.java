package org.theancients.placebackend.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.anonymous_session.AnonymousSessionService;
import org.theancients.placebackend.authentication.AuthenticationService;
import org.theancients.placebackend.authentication.PendingAuthentication;
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

    @Autowired
    private AuthenticationService authenticationService;

    public StatusResponseDto statusUpdate(StatusRequestDto statusRequestDto) {
        String sessionId = statusRequestDto.getSessionId();
        boolean sessionValid = anonymousSessionService.refreshSession(sessionId);
        if (sessionValid) {
            StatusResponseDto statusResponseDto = new StatusResponseDto();

            PendingAuthentication pendingAuthentication = authenticationService.getPendingAuthentication(sessionId);
            if (pendingAuthentication != null) {
                statusResponseDto.setIdentity(pendingAuthentication.getIdentity());
                statusResponseDto.setAuthToken(pendingAuthentication.getAuthToken());
            }

            List<Point> highlights = highlightService.updateHighlight(sessionId, statusRequestDto.getHighlightPos());
            statusResponseDto.setHighlights(highlights);
            statusResponseDto.setPixelGrid(pixelGridService.getPixelGrid());

            return statusResponseDto;
        }

        return null;
    }

}
