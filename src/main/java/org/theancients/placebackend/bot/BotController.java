package org.theancients.placebackend.bot;

import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bot")
public class BotController {

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
    public String botMessage(@RequestBody BotMessageDto botMessageDto) {
        return "{}";
    }

}
