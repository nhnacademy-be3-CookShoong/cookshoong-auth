package store.cookshoong.www.cookshoongauth.service;

/**
 * Refresh 토큰이 아닌 토큰이 들어왔을 떄 던져지는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.07.20
 */
public class RefreshTokenValidationException extends RuntimeException {
    public RefreshTokenValidationException() {
        super("토큰의 형식을 확인해주세요.");
    }
}
