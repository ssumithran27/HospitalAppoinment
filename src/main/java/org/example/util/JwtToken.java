package org.example.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.SecretKey;
import java.util.Date;


public class JwtToken {
        private static final Logger log = LoggerFactory.getLogger(JwtToken.class);
        private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("This the key for jwt token generation".getBytes());
        private static final long EXPIRATION_TIME = (long) 30 * 60 * 1000;
        private  JwtToken() {
        }

        public static String generateToken(String username) {
            log.info("token generation started");
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();
        }

        public static String extractUsername(String token) {
            return getClaims(token).getSubject();
        }

        public static boolean isTokenExpired(String token) {
            return getClaims(token).getExpiration().before(new Date());
        }

    public static boolean validateToken(String token, String username) {
            try{
        return extractUsername(token).equals(username) && !isTokenExpired(token);
            }catch(JwtException e){
                return false;
            }

    }

        private static Claims getClaims(String token) {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }



    }

