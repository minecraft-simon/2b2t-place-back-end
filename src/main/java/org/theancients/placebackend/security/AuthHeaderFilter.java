package org.theancients.placebackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.theancients.placebackend.authentication.AuthenticationService;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
public class AuthHeaderFilter extends OncePerRequestFilter {

    @Value("${application.botKey}")
    private String botKey;

    private final SecretKey secretKey;

    public AuthHeaderFilter(@Lazy SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && botKey != null) {
            if (!authorization.startsWith("Bearer ")) {
                // try to authenticate as bot
                if (authorization.equals(botKey)) {
                    Set<GrantedAuthority> authorities = new HashSet<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_BOT"));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                // try to authenticate as player
                String token = authorization.replace("Bearer ", "");
                try {
                    // throws exception if token is invalid
                    Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
                    Claims body = claimsJws.getBody();
                    String identity = body.getSubject();
                    Set<GrantedAuthority> authorities = new HashSet<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_AUTHENTICATED_USER"));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(identity, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (JwtException ignored) {
                    response.setStatus(401);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
