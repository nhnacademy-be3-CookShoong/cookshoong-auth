package store.cookshoong.www.cookshoongauth.exeption;

/**
 * OAuth 에서 건네받은 회원식별자가 누락됐을 때 발생하는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.08.01
 */
public class InvalidAccountCodeException extends RuntimeException {
    public InvalidAccountCodeException() {
        super("헤더값을 확인해주세요");
    }
}
