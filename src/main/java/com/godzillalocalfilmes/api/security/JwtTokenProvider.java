package com.godzillalocalfilmes.api.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import com.godzillalocalfilmes.api.model.Role;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtTokenProvider {

        @Value("${jwt.secret}")
        private String jwtSecret;

        private final long JWT_EXPIRATION = 604800000L; // 7 dias

        private Key getSigningKey() {
                byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
                return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
        }

        public String generateToken(String email, List<Role> roles) {
                Date now = new Date();
                Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

                String rolesString = roles.stream()
                                .map(Role::getName)
                                .collect(Collectors.joining(","));

                return Jwts.builder()
                                .setSubject(email)
                                .claim("roles", rolesString)
                                .setIssuedAt(now)
                                .setExpiration(expiryDate)
                                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                                .compact();
        }

        // Método para obter o email a partir do token
        public String getEmailFromToken(String token) {
                return Jwts.parserBuilder()
                                .setSigningKey(getSigningKey())
                                .build()
                                .parseClaimsJws(token)
                                .getBody()
                                .getSubject();
        }

        // Método para obter as roles a partir do token
        public List<String> getRolesFromToken(String token) {
                String roles = (String) Jwts.parserBuilder()
                                .setSigningKey(getSigningKey())
                                .build()
                                .parseClaimsJws(token)
                                .getBody()
                                .get("roles");
                return List.of(roles.split(","));
        }

        // Método para validar o token
        public boolean validateToken(String token) {
                try {
                        Jwts.parserBuilder()
                                        .setSigningKey(getSigningKey())
                                        .build()
                                        .parseClaimsJws(token);
                        return true;
                } catch (JwtException | IllegalArgumentException e) {
                        // Token inválido ou expirado
                        return false;
                }
        }
}
