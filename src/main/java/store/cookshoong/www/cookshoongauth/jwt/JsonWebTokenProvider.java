package store.cookshoong.www.cookshoongauth.jwt;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 특정 정보를 받아 JWT(Json Web Token)을 만들어주는 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */
@Component
@RequiredArgsConstructor
public class JsonWebTokenProvider {
    private final JwtProperties jwtProperties;

    /**
     * 토큰 식별자와 회원의 권한을 담은 액세스 토큰을 만든다.
     *
     * @param jti       the jti
     * @param authority the authority
     * @return the string
     */
    public String createAccessToken(String jti, String authority) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("jti", jti);
        payloads.put("authority", authority);
        return createAccessToken(payloads);
    }

    private String createAccessToken(Map<String, Object> payloads) {
        return JwtFactory.createToken(Map.of(), payloads, jwtProperties.getSecret(), jwtProperties.getAccessTokenTtl());
    }

    /**
     * 토큰 식별자와 회원 시퀀스, 회원 상태, 회원의 아이디를 담은 액세스 토큰을 만든다.
     *
     * @param jti       the jti
     * @param accountId the account id
     * @param status    the status
     * @param loginId   the login id
     * @return the string
     */
    public String createRefreshToken(String jti, String accountId, String status, String loginId) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("jti", jti);
        payloads.put("accountId", accountId);
        payloads.put("status", status);
        payloads.put("loginId", loginId);
        return createRefreshToken(payloads);
    }

    private String createRefreshToken(Map<String, Object> payloads) {
        return JwtFactory.createToken(Map.of(), payloads, jwtProperties.getSecret(),
            jwtProperties.getRefreshTokenTtl());
    }
}
