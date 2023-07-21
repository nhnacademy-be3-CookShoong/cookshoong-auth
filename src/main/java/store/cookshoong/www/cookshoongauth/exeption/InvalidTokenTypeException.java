package store.cookshoong.www.cookshoongauth.exeption;

/**
 * 토큰 타입이 정해진 타입(Bearer)이 아닐 때 발생하는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.07.21
 */
public class InvalidTokenTypeException extends RuntimeException {
    public InvalidTokenTypeException(String msg) {
        super(msg + "는 잘못된 토큰 형식입니다.");
    }
}
