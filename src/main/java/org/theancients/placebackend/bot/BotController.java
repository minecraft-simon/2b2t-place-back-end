package org.theancients.placebackend.bot;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theancients.placebackend.status.StatusRequestDto;
import org.theancients.placebackend.status.StatusResponseDto;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/bot")
public class BotController {

    @PutMapping
    @RolesAllowed("ROLE_BOT")
    public BotStatusResponseDto botStatusUpdate(@RequestBody BotStatusRequestDto botStatusRequestDto) {
        return new BotStatusResponseDto();
    }

}
