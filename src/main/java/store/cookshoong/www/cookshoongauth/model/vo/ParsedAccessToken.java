package store.cookshoong.www.cookshoongauth.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파싱된 액세스 토큰을 다루기 위한 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.16
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParsedAccessToken {
    private String jti;
    private String authority;
    private String exp;
}
