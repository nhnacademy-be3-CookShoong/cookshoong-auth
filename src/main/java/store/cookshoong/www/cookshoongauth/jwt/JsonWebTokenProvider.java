package store.cookshoong.www.cookshoongauth.jwt;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongauth.util.JwtFactory;

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

    public String createAccessToken(String jid, String authority) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("jid", jid);
        payloads.put("authority", authority);
        return createAccessToken(payloads);
    }

    public String createRefreshToken(String jid, String accountId, String status, String loginId) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("jid", jid);
        payloads.put("accountId", accountId);
        payloads.put("status", status);
        payloads.put("loginId", loginId);
        return createRefreshToken(payloads);
    }

    private String createAccessToken(Map<String, Object> payloads) {
        return JwtFactory.createToken(Map.of(), payloads, jwtProperties.getSecret(), jwtProperties.getAccessTokenTtl());
    }
    private String createRefreshToken(Map<String, Object> payloads) {
        return JwtFactory.createToken(Map.of(), payloads, jwtProperties.getSecret(), jwtProperties.getRefreshTokenTtl());
    }
}
