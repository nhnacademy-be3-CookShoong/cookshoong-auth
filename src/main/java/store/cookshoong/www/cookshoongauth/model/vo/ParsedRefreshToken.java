package store.cookshoong.www.cookshoongauth.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 읽기 좋은 형태로 변환된 리프레쉬 토큰.
 *
 * @author koesnam (추만석)
 * @since 2023.07.17
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParsedRefreshToken {
    private String jti;
    private Long exp;
    private Long accountId;
    private String status;
    private String loginId;
    private String authority;
}
