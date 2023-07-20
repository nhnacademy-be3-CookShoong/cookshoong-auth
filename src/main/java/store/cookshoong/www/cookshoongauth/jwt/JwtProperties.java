package store.cookshoong.www.cookshoongauth.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Jwt 관련 설정값들을 담고 있는 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtProperties {
    private JwtSecret jwtSecret;
    private JwtTtl jwtTtl;
}
