package org.theancients.placebackend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("request-auth-code/{sessionId}")
    public AuthCodeDto requestAuthCode(@PathVariable String sessionId) {
        return authenticationService.generateAuthCode(sessionId);
    }

    @DeleteMapping("invalidate-pending-auth/{sessionId}")
    public String invalidatePending(@PathVariable String sessionId) {
        authenticationService.deletePendingAuthentication(sessionId);
        return "{}";
    }

}
