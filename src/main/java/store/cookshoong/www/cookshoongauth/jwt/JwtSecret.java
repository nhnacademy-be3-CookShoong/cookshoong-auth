package store.cookshoong.www.cookshoongauth.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Jwt 에 서명을 위한 비밀키.
 *
 * @author koesnam (추만석)
 * @since 2023.07.19
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class JwtSecret {
    private String accessSecret;
    private String refreshSecret;
}
