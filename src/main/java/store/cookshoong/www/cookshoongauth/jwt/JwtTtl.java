package store.cookshoong.www.cookshoongauth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰들의 유효기간 설정들을 모아놓은 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.19
 */
@Getter
@AllArgsConstructor
public class JwtTtl {
    private final Long accessTokenTtl;
    private final Long refreshTokenTtl;
}
