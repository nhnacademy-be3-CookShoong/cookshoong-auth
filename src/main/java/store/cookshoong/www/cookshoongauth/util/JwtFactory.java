package store.cookshoong.www.cookshoongauth.util;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

/**
 * Jwt 를 생성만을 담당하는 유틸클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */
public class JwtFactory {
    private JwtFactory() {}

    /**
     * Jwt 토큰을 발행하는 메서드.
     *
     * @param headers  the headers
     * @param payloads the payloads
     * @param secret   the secret
     * @param ttl      the ttl
     * @return the string
     */
    public static String createToken(Map<String, Object> headers, Map<String, Object> payloads, String secret,
                                     int ttl) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + ttl);

        return Jwts.builder()
            .setHeader(headers)
            .setClaims(payloads)
            .setExpiration(expireTime)
            .signWith(key)
            .compact();
    }
}
