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
    private AuthenticationService authenticationService;

    @PutMapping
    @RolesAllowed("ROLE_BOT")
    public BotStatusResponseDto botStatusUpdate(@RequestBody BotStatusRequestDto botStatusRequestDto) {
        List<int[]> jobs = new ArrayList<>();
        jobs.add(new int[]{2273, 17, 97, 97, 1});
        jobs.add(new int[]{7218, 56, 97, 50, 1});
        jobs.add(new int[]{4473, 34, 97, 121, 1});
        jobs.add(new int[]{11415, 89, 97, 23, 1});
        jobs.add(new int[]{16261, 127, 97, 5, 1});
        BotStatusResponseDto response = new BotStatusResponseDto();
        response.setJobs(jobs);
        return response;
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
