package store.cookshoong.www.cookshoongauth.jwt;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.RequiredTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.cookshoong.www.cookshoongauth.model.vo.ParsedRefreshToken;

/**
 * Jwt 동작과 관련된 테스트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.19
 */
class JwtFactoryTest {

    JsonWebTokenProvider jwtProvider;
    JwtSecret jwtSecret;
    @BeforeEach
    void setup() {
        jwtSecret = new JwtSecret("qwertyuiopasdfghjklzxcvbnm123456", "qwertyuiopasdfghjklzxcvbnm123456");
        JwtTtl jwtTtl = new JwtTtl(3600L, 3600L);

        JwtProperties jwtProperties = new JwtProperties(jwtSecret, jwtTtl);

        jwtProvider = new JsonWebTokenProvider(jwtProperties);
    }

    @Test
    void parseToken() {
        String selfMadeJwt = jwtProvider.createAccessToken("1", "CUSTOMER");
        Claims selfMadeClaims = JwtFactory.extractFrom(jwtSecret.getAccessSecret(), selfMadeJwt);

        assertThat(selfMadeClaims.get("jti"), is("1"));
        assertThat(selfMadeClaims.get("authority"), is("CUSTOMER"));
    }

    @Test
    void parseToken2() {
        String selfMadeJwt = jwtProvider.createRefreshToken("1", "2", "ACTIVE", "user1",
            "CUSTOMER");
        Claims selfMadeClaims = JwtFactory.extractFrom(jwtSecret.getRefreshSecret(), selfMadeJwt);
        ObjectMapper objectMapper = new ObjectMapper();

        ParsedRefreshToken parsedRefreshToken = objectMapper.convertValue(selfMadeClaims, ParsedRefreshToken.class);
        assertThat(parsedRefreshToken.getJti(), is(selfMadeClaims.get("jti")));
        assertThat(parsedRefreshToken.getStatus(), is(selfMadeClaims.get("status")));
        assertThat(parsedRefreshToken.getLoginId(), is(selfMadeClaims.get("loginId")));
        assertThat(parsedRefreshToken.getExp(), is(selfMadeClaims.get("exp", Long.class)));
        // ISSUE: accountId는 Long.class로 변환되지 않은 이슈있음.
        assertThatThrownBy(() -> selfMadeClaims.get("accountId", Long.class))
            .isInstanceOf(RequiredTypeException.class);
        assertThat(parsedRefreshToken.getAccountId(), is(Long.valueOf((String) selfMadeClaims.get("accountId"))));
        assertDoesNotThrow(() -> selfMadeClaims.get("exp", Long.class));
    }
}
