package org.theancients.placebackend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("request-auth-code")
    public AuthCodeDto requestAuthCode(@RequestBody Map<String, String> body) {
        String sessionId = body.get("sessionId");
        String authServer = body.get("authServer");
        if (sessionId == null || authServer == null) {
            return null;
        } else {
            return authenticationService.generateAuthCode(sessionId, authServer);
        }
    }

    @DeleteMapping("invalidate-pending-auth/{sessionId}")
    public String invalidatePending(@PathVariable String sessionId) {
        authenticationService.deletePendingAuthentication(sessionId);
        return "{}";
    }

}
