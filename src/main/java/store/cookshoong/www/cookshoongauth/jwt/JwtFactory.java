package store.cookshoong.www.cookshoongauth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
                                     Long ttl) {
        SecretKey key = getKey(secret);

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + ttl);

        return Jwts.builder()
            .setHeader(headers)
            .setClaims(payloads)
            .setExpiration(expireTime)
            .signWith(key)
            .compact();
    }

    /**
     * 원본 Jwt 스트링으로부터 값이 들어있는 바디(Claims)를 추출해낸다.
     *
     * @param secret   the secret
     * @param rawToken the raw token
     * @return the claims
     * @throws ExpiredJwtException      토큰 만료
     * @throws UnsupportedJwtException  Jwt 형식이 아닌 다른 형식
     * @throws MalformedJwtException    올바르지 않은 토큰 값
     * @throws SignatureException       서명 실패
     * @throws IllegalArgumentException 올바르지 않은 토큰 입력값
     */
    public static Claims extractFrom(String secret, String rawToken) throws ExpiredJwtException,
        UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parserBuilder()
            .setSigningKey(getKey(secret))
            .build()
            .parseClaimsJws(rawToken)
            .getBody();
    }

    private static SecretKey getKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
