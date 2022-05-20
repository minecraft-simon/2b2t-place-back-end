package org.theancients.placebackend.data_endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.theancients.placebackend.job.JobDto;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("current-state")
    @RolesAllowed("ROLE_BOT")
    public CurrentStateDto getCurrentState() {
        return dataService.getCurrentState();
    }

}
