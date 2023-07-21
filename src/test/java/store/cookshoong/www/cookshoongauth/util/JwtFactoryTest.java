package store.cookshoong.www.cookshoongauth.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.jsonwebtoken.security.WeakKeyException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.cookshoong.www.cookshoongauth.jwt.JwtFactory;

/**
 * Jwt 생성결과를 확인하기 위한 테스트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */
class JwtFactoryTest {
    @Test
    @DisplayName("Jwt는 헤더, 페이로드, 서명 세 부분으로 나뉘고 .으로 구분된다.")
    void test1() {
        String encodedJwt = JwtFactory.createToken(Map.of(), Map.of("accountId", 1L),
            "TempTokenMustBeLargerThan256Bit!!", 10L);

        assertThat(encodedJwt.split("\\.").length, is(3));
    }

    @Test
    @DisplayName("Secret 을 키로 만들 때 암호화 알고리즘이 사용된다." +
        " 각 암호화 알고리즘에 필요한 Secret 의 길이가 있으며 HMAC-SHA 알고리즘에서는 256 bit가 넘어야 한다. ")
    void test2() {
        assertDoesNotThrow(() -> JwtFactory.createToken(Map.of(), Map.of("accountId", 1L),
            "TokenSecretMustBeLargerThan256Bit!!", 10L));
    }

    @Test
    @DisplayName("HMAC-SHA 알고리즘은 256bit 가 넘어야 한다. ")
    void test3() {
        assertThrows(WeakKeyException.class,
            () -> JwtFactory.createToken(Map.of(), Map.of("accountId", 1L),
                "ItislessThan256Bit", 10L));
    }
}
