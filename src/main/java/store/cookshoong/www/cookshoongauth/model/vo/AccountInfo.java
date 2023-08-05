package store.cookshoong.www.cookshoongauth.model.vo;

import lombok.Getter;
import store.cookshoong.www.cookshoongauth.model.response.AccountInfoResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.AuthenticationResponseDto;

/**
 * 인증된 정보로부터 회원정보를 얻어내는 클래스. 현재 토큰 발급에 사용되고 있다.
 *
 * @author koesnam (추만석)
 * @since 2023.07.15
 */
@Getter
public class AccountInfo {
    private final String accountId;
    private final String loginId;
    private final String authority;
    private final String status;

    /**
     * 인증조회정보를 기반으로 회원정보 생성.
     *
     * @param dto the dto
     */
    public AccountInfo(AuthenticationResponseDto dto) {
        this.accountId = String.valueOf(dto.getAttributes().get("accountId"));
        this.loginId = dto.getLoginId();
        this.authority = (String) dto.getAttributes().get("authority");
        this.status = (String) dto.getAttributes().get("status");
    }

    /**
     * OAuth2 로그인 응답 객체를 통해 회원정보 생성.
     *
     * @param dto the dto
     */
    public AccountInfo(AccountInfoResponseDto dto) {
        this.accountId = dto.getAccountId();
        this.loginId = dto.getLoginId();
        this.authority = dto.getAuthority();
        this.status = dto.getStatus();
    }
}
