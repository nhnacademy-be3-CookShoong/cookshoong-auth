package store.cookshoong.www.cookshoongauth.model.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * OAuth2 회원 정보 응답 객체.
 *
 * @author koesnam (추만석)
 * @since 2023.08.01
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountInfoResponseDto {
    private String accountId;
    private String loginId;
    private String authority;
    private String status;
}
