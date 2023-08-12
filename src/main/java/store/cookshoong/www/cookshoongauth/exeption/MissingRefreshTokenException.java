package store.cookshoong.www.cookshoongauth.exeption;

/**
 * 토큰 재발급시 리프레쉬 토큰이 없을 때 발생하는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.08.12
 */
public class MissingRefreshTokenException extends RuntimeException {
    public MissingRefreshTokenException() {
        super("리프레쉬 토큰이 필요합니다.");
    }
}
