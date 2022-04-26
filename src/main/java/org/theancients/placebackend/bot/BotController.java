package org.theancients.placebackend.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.theancients.placebackend.authentication.AuthenticationService;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    private BotService botService;

    @Autowired
    private AuthenticationService authenticationService;

    @PutMapping
    @RolesAllowed("ROLE_BOT")
    public ResponseEntity<BotStatusResponseDto> botStatusUpdate(@RequestBody BotStatusRequestDto botStatusRequestDto) {
        BotStatusResponseDto response = botService.updateStatus(botStatusRequestDto);
        if (response == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("message")
    @RolesAllowed("ROLE_BOT")
    public ResponseEntity<String> botMessage(@RequestBody BotMessageDto dto) {
        if (dto == null || dto.getUsername() == null || dto.getUsername().isBlank()
                || dto.getPlayer() == null || dto.getPlayer().isBlank()
                || dto.getMessage() == null || dto.getMessage().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        authenticationService.tryToAuthenticate(dto.getPlayer(), dto.getMessage());
        return ResponseEntity.ok("{}");
    }

}
