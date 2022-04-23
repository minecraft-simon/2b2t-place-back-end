package org.theancients.placebackend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.anonymous_session.AnonymousSessionRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthenticationService {

    @Autowired
    private PendingAuthenticationRepository pendingAuthenticationRepository;

    @Autowired
    private AnonymousSessionRepository anonymousSessionRepository;

    @Scheduled(fixedRate = 30000)
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
        if (sessionId != null && anonymousSessionRepository.existsById(sessionId)) {
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

    public void tryToAuthenticate(String player, String message) {
        message = message.toUpperCase();
        Optional<PendingAuthentication> optionalPendingAuthentication = pendingAuthenticationRepository.findByAuthCode(message);
        if (optionalPendingAuthentication.isPresent()) {
            PendingAuthentication pendingAuthentication = optionalPendingAuthentication.get();
            if (pendingAuthentication.getIdentity() == null) {
                pendingAuthentication.setIdentity(player);
                pendingAuthenticationRepository.save(pendingAuthentication);
            }
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
