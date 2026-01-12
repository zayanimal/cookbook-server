package ru.rabbit.cookbook.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Утилита для работы с JWT токенами
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLong}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration; // 24 часа по умолчанию

    /**
     * Генерирует JWT токен для пользователя
     *
     * @param username имя пользователя
     * @param role роль пользователя
     * @return JWT токен
     */
    public String generateToken(final String username, final String role) {
        val now = new Date();
        val expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .subject(username)
            .claim("role", role)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey())
            .compact();
    }

    /**
     * Извлекает имя пользователя из токена
     *
     * @param token JWT токен
     * @return имя пользователя
     */
    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Извлекает роль из токена
     *
     * @param token JWT токен
     * @return роль пользователя
     */
    public String getRoleFromToken(final String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    /**
     * Проверяет валидность токена
     *
     * @param token JWT токен
     * @return true если токен валиден
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Извлекает claim из токена
     *
     * @param token JWT токен
     * @param claimsResolver функция для извлечения claim
     * @param <T> тип claim
     * @return значение claim
     */
    private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        val claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все claims из токена
     *
     * @param token JWT токен
     * @return все claims
     */
    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * Получает ключ для подписи токена
     *
     * @return секретный ключ
     */
    private SecretKey getSigningKey() {
        val keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
