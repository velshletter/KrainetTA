package com.velshletter.notification_service.security;

import com.velshletter.notification_service.mail.MailProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtServiceTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String getServiceToken() {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 10 * 60 * 1000);

        var tok = Jwts.builder()
            .subject("notification-service")
            .claim("role", "ROLE_INTERNAL_SERVICE")
            .issuedAt(now)
            .expiration(expiry)
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
        log.info(tok);
        return tok;
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
