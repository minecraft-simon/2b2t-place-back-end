package org.theancients.placebackend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthenticationService {

    @Autowired
    private PendingAuthenticationRepository pendingAuthenticationRepository;

    public AuthCodeDto generateAuthCode(String sessionId) {
        if (sessionId != null) {
            String authCode = generateAuthCode();
            AuthCodeDto authCodeDto = new AuthCodeDto();
            authCodeDto.setAuthCode(authCode);
            return authCodeDto;
        } else {
            return null;
        }
    }

    private String generateAuthCode() {
        long existingCount = pendingAuthenticationRepository.count();
        int length = Math.max((int) Math.ceil(Math.log10(existingCount)), 3);
        String allowedChars = "CFGHKMNPTWXY"; // no chars that look similar to numbers or to each other
        String authCode;
        do {
            authCode = "";
            for (int i = 0; i < length; i++) {
                int rand = ThreadLocalRandom.current().nextInt(0, allowedChars.length());
                authCode += allowedChars.charAt(rand);
            }
        } while (pendingAuthenticationRepository.existsById(authCode)); // repeat if auth code is already in use
        return authCode;
    }

}
