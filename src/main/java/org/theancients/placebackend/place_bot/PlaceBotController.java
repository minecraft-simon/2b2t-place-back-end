package org.theancients.placebackend.place_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.theancients.placebackend.authentication.AuthenticationService;
import org.theancients.placebackend.job.JobDto;
import org.theancients.placebackend.job.JobService;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/place-bot")
public class PlaceBotController {

    @Autowired
    private PlaceBotService placeBotService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JobService jobService;

    @PutMapping
    @RolesAllowed("ROLE_BOT")
    public ResponseEntity<PlaceBotStatusResponseDto> placeBotStatusUpdate(@RequestBody PlaceBotStatusRequestDto placeBotStatusRequestDto) {
        PlaceBotStatusResponseDto response = placeBotService.updateStatus(placeBotStatusRequestDto);
        if (response == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("finished-job")
    @RolesAllowed("ROLE_BOT")
    public String finishedJob(@RequestBody JobDto jobDto) {
        jobService.jobFinished(jobDto);
        return "{}";
    }

}
