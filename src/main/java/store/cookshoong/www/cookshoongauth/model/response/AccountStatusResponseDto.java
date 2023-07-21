package store.cookshoong.www.cookshoongauth.model.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원의 현재 상태조회에 대한 응답 객체.
 *
 * @author koesnam (추만석)
 * @since 2023.07.19
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountStatusResponseDto {
    private String status;
}
