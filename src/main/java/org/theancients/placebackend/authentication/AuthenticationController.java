package org.theancients.placebackend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("request-auth-code/{sessionId}")
    public AuthCodeDto requestAuthCode(@PathVariable String sessionId) {
        return authenticationService.generateAuthCode(sessionId);
    }

}
