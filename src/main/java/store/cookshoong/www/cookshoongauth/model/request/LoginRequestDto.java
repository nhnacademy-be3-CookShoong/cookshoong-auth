package store.cookshoong.www.cookshoongauth.model.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Front-server 에서 들어오는 로그인을 위한 자격증명정보.
 *
 * @author koesnam (추만석)
 * @since 2023.07.13
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
}
