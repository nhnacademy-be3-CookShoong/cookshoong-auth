package store.cookshoong.www.cookshoongauth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongauth.model.vo.ParsedAccessToken;
import store.cookshoong.www.cookshoongauth.model.vo.ParsedRefreshToken;

/**
 * 전송받은 Jwt 를 검증하기 위한 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.08.25
 */
@Component
@RequiredArgsConstructor
public class JwtValidator {
    private final JwtProperties jwtProperties;

    /**
     * 액세스 토큰과 리프레쉬 토큰이 같이 발급된 토큰인지를 검증하는 메서드.
     * 매 토큰 발행시 두 토큰은 같은 jti 를 가지므로 jti 가 다르다면 문제가 있는 토큰이므로 해당 부분을 거르기 위해 작성됨.
     *
     * @param accessToken  the access token
     * @param refreshToken the refresh token
     * @return the boolean
     */
    public boolean validatePairToken(String accessToken, String refreshToken) {
        if (!validateAccessTokenWithoutExpired(accessToken) || !validateRefreshToken(refreshToken)) {
            return false;
        }
        ParsedAccessToken parsedAccessToken = JwtResolver.resolveAccessToken(accessToken);
        ParsedRefreshToken parsedRefreshToken = JwtResolver.resolveRefreshToken(refreshToken);

        return isJtiOfTokensEqual(parsedAccessToken, parsedRefreshToken);
    }

    private boolean isJtiOfTokensEqual(ParsedAccessToken accessToken, ParsedRefreshToken refreshToken) {
        return accessToken.getJti().equals(refreshToken.getJti());
    }

    private boolean validateAccessTokenWithoutExpired(String accessToken) {
        try {
            JwtFactory.extractFrom(jwtProperties.getJwtSecret().getAccessSecret(), accessToken);
        } catch (ExpiredJwtException ignore) {
            // 인증서버에서 발급한 액세스토큰이 맞는지만 확인하기 때문에 만료 검증 x
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e2) {
            return false;
        }
        return true;
    }

    private boolean validateRefreshToken(String refreshToken) {
        try {
            JwtFactory.extractFrom(jwtProperties.getJwtSecret().getRefreshSecret(), refreshToken);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
