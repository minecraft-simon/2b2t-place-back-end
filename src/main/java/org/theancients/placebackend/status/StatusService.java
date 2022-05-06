package org.theancients.placebackend.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.anonymous_session.AnonymousSessionService;
import org.theancients.placebackend.authentication.AuthenticationService;
import org.theancients.placebackend.authentication.PendingAuthentication;
import org.theancients.placebackend.highlight.HighlightService;
import org.theancients.placebackend.pixel_grid.PixelGridService;
import org.theancients.placebackend.player.PlayerService;
import org.theancients.placebackend.setting.SettingService;

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

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SettingService settingService;

    public StatusResponseDto statusUpdate(String username, StatusRequestDto statusRequestDto) {
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

            statusResponseDto.setPollingDelay(settingService.getInt("frontend_polling_delay", 1000));
            statusResponseDto.setCooldownSeconds(playerService.getCooldownSeconds());
            statusResponseDto.setCooldownSecondsLeft(playerService.getCooldownSecondsLeft(username));

            statusResponseDto.setMaintenanceMode(settingService.getBoolean("maintenance_mode", false));

            return statusResponseDto;
        }

        return null;
    }

}
