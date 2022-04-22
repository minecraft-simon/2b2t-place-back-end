package org.theancients.placebackend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.anonymous_session.AnonymousSession;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthenticationService {

    @Autowired
    private PendingAuthenticationRepository pendingAuthenticationRepository;

    @Scheduled(fixedRate = 3000)
    private void deleteExpiredPendingAuths() {
        List<PendingAuthentication> pending = pendingAuthenticationRepository.findAll();
        List<PendingAuthentication> toDelete = new ArrayList<>();
        Instant now = Instant.now();
        for (PendingAuthentication auth : pending) {
            if (now.isAfter(auth.getExpiry())) {
                toDelete.add(auth);
            }
        }
        pendingAuthenticationRepository.deleteAll(toDelete);
    }

    public AuthCodeDto generateAuthCode(String sessionId) {
        if (sessionId != null) {
            // create pending authentication
            String authCode = generateAuthCode();
            PendingAuthentication pendingAuthentication = new PendingAuthentication(sessionId, authCode);
            pendingAuthenticationRepository.save(pendingAuthentication);

            AuthCodeDto authCodeDto = new AuthCodeDto();
            authCodeDto.setAuthCode(authCode);
            authCodeDto.setExpirySeconds(Duration.between(Instant.now(), pendingAuthentication.getExpiry()).toSeconds());
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
        } while (pendingAuthenticationRepository.existsByAuthCode(authCode)); // repeat if auth code is already in use
        return authCode;
    }

}
