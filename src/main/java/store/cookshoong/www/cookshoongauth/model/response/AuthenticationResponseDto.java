package store.cookshoong.www.cookshoongauth.model.response;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API(Backend-server) 를 통해 가져온 회원정보.
 *
 * @author koesnam (추만석)
 * @since 2023.07.13
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationResponseDto {
    private String loginId;
    private String password;
    private Map<String, Object> attributes;
}

