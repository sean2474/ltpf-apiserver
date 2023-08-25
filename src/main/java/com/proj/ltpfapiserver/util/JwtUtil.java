package com.proj.ltpfapiserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Date;

import com.proj.ltpfapiserver.model.User;

public class JwtUtil {

    private static final String SECRET_KEY = "$HoomC(=Ij^5#(4r6=,'jG(JaD4qP5TOe'AOM:#L|/!Kq";

    public static Claims decode(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt);
            return claimsJws.getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature");
        }
    }

    public static String generateToken(User user) {
        final long JWT_EXPIRATION = 86400000L; // 1 day in milliseconds

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
