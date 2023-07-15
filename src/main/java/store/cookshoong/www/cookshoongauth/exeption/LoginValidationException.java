package store.cookshoong.www.cookshoongauth.exeption;

/**
 * 아이디나 비밀번호에 빈 값이 들어온 경우에 일어나는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.07.15
 */
public class LoginValidationException extends RuntimeException {
    public LoginValidationException() {
        super("아이디 또는 비밀번호를 확인해주세요.");
    }
}
