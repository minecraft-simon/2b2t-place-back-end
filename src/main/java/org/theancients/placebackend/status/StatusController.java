package org.theancients.placebackend.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @PutMapping
    public ResponseEntity<StatusResponseDto> statusUpdate(Principal principal, @RequestBody StatusRequestDto statusRequestDto) {
        String username = principal == null ? null : principal.getName();
        StatusResponseDto statusResponseDto = statusService.statusUpdate(username, statusRequestDto);
        if (statusResponseDto != null) {
            return ResponseEntity.ok(statusResponseDto);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
