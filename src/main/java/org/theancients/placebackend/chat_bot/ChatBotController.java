package org.theancients.placebackend.chat_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.theancients.placebackend.authentication.AuthenticationService;
import org.theancients.placebackend.place_bot.PlaceBotService;
import org.theancients.placebackend.job.JobService;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/chat-bot")
public class ChatBotController {

    @Autowired
    private PlaceBotService placeBotService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JobService jobService;

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
