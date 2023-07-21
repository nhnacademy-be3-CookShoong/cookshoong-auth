package store.cookshoong.www.cookshoongauth.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 인증성공시 Front-server 로 보내줄 정보.
 *
 * @author koesnam (추만석)
 * @since 2023.07.15
 */

@Getter
@AllArgsConstructor
public class TokenReissueResponseDto {
    private String accessToken;
    private String refreshToken;
}
