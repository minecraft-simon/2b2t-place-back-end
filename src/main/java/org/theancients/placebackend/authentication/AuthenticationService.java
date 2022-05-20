package org.theancients.placebackend.authentication;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.anonymous_session.AnonymousSessionRepository;
import org.theancients.placebackend.player.PlayerService;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthenticationService {

    @Autowired
    private PendingAuthenticationRepository pendingAuthenticationRepository;

    @Autowired
    private AnonymousSessionRepository anonymousSessionRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SecretKey secretKey;

    @Scheduled(fixedRate = 5000)
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

    public AuthCodeDto generateAuthCode(String sessionId, String authServer) {
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

    private synchronized String generateAuthCode() {
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

    public void tryToAuthenticate(String player, String message) {
        message = message.toUpperCase();
        Optional<PendingAuthentication> optionalPendingAuthentication = pendingAuthenticationRepository.findByAuthCode(message);
        if (optionalPendingAuthentication.isPresent()) {
            PendingAuthentication pendingAuth = optionalPendingAuthentication.get();
            if (pendingAuth.getIdentity() == null && pendingAuth.getAuthToken() == null) {
                pendingAuth.setIdentity(player);
                pendingAuth.setAuthToken(generateAuthToken(player));
                pendingAuthenticationRepository.save(pendingAuth);
                playerService.createPlayer(player);
            }
        }
    }

    public PendingAuthentication getPendingAuthentication(String sessionId) {
        return pendingAuthenticationRepository.findBySessionId(sessionId).orElse(null);
    }

    public String generateAuthToken(String player) {
        String token = Jwts.builder()
                .setSubject(player)
                .setIssuedAt(new Date())
                .signWith(secretKey)
                .compact();
        return "Bearer " + token;
    }

    public void deletePendingAuthentication(String sessionId) {
        if (sessionId != null && pendingAuthenticationRepository.existsBySessionId(sessionId)) {
            pendingAuthenticationRepository.deleteById(sessionId);
        }
    }

}
