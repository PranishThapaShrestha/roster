package com.nff.roster.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {

    private final Key key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    public String generateToken(String subject) {

        Instant currentTime = Instant.now();

        return Jwts.builder().setSubject(subject)
                .setIssuedAt(Date.from(currentTime))
                .setExpiration(Date.from(currentTime.plusMillis(expirationMs)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public Jws<Claims> parse(String jwtToken) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken);

    }

//    public void giveKey(){
//
//      System.out.println(expirationMs);
//    }

}
