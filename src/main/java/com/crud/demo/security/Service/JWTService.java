package com.crud.demo.security.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    private static final Logger log = LoggerFactory.getLogger(JWTService.class);

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extrairUsername(String token) {
        log.debug("[extrairUsername] Extraindo o nome de usuário do token.");
        return extrairClaim(token, Claims::getSubject);
    }

    public <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        log.debug("[extrairClaim] Extraindo claim do token.");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String gerarToken(UserDetails userDetails) {
        log.debug("[gerarToken] Gerando o token para o usuário: {}", userDetails.getUsername());
        return gerarToken(new HashMap<>(), userDetails);
    }

    public String gerarToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.debug("[gerarToken] Adicionando claims adicionais e gerando token.");
        extraClaims.put("role", "USER");
        extraClaims.put("created_at", new Date());

        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public long getTempoExpiracao() {
        log.debug("[getTempoExpiracao] Retornando tempo de expiração do token.");
        return jwtExpiration;
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        log.debug("[buildToken] Construindo o token JWT com claims adicionais.");
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValido(String token, UserDetails userDetails) {
        log.debug("[isTokenValido] Verificando se o token é válido.");
        final String username = extrairUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpirado(token);
    }

    private boolean isTokenExpirado(String token) {
        log.debug("[isTokenExpirado] Verificando se o token expirou.");
        return extrairExpiracao(token).before(new Date());
    }

    public Date extrairExpiracao(String token) {
        log.debug("[extrairExpiracao] Extraindo a data de expiração do token.");
        return extrairClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        log.debug("[extractAllClaims] Extraindo todas as claims do token.");
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extrairRole(String token) {
        log.debug("[extrairRole] Extraindo o papel do usuário do token.");
        return extrairClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extrairCreatedAt(String token) {
        log.debug("[extrairCreatedAt] Extraindo a data de criação do token.");
        return extrairClaim(token, claims -> claims.get("created_at", Date.class));
    }

    private Key getSignInKey() {
        log.debug("[getSignInKey] Obtendo a chave secreta para assinar o token.");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
