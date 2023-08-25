package store.cookshoong.www.cookshoongauth.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Base64Utils;
import store.cookshoong.www.cookshoongauth.exeption.InvalidAccessTokenException;
import store.cookshoong.www.cookshoongauth.model.vo.ParsedAccessToken;
import store.cookshoong.www.cookshoongauth.model.vo.ParsedRefreshToken;

/**
 * Jwt 를 읽기 좋은 형태로 바꿔주는 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.16
 */
public class JwtResolver {
    private JwtResolver() {}

    public static ParsedAccessToken resolveAccessToken(String accessToken) {
        return resolveToken(accessToken, ParsedAccessToken.class);
    }

    public static ParsedRefreshToken resolveRefreshToken(String refreshToken) {
        return resolveToken(refreshToken, ParsedRefreshToken.class);
    }

    /**
     * Token 값을 디코딩하여 읽기 좋은 형태로 바꾸는 메서드.
     *
     * @param <T>       the type parameter
     * @param token     the token
     * @param tokenType the token type
     * @return the access token
     */
    private static <T> T resolveToken(String token, Class<T> tokenType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resolveToken(token), tokenType);
        } catch (JsonProcessingException e) {
            throw new InvalidAccessTokenException();
        }
    }

    private static String resolveToken(String token) {
        String payload = token.split("\\.")[1];
        return new String(Base64Utils.decodeFromString(payload));
    }
}
