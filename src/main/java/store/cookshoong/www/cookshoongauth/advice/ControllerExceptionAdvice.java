package store.cookshoong.www.cookshoongauth.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import store.cookshoong.www.cookshoongauth.exeption.InvalidAccountCodeException;
import store.cookshoong.www.cookshoongauth.exeption.InvalidTokenTypeException;
import store.cookshoong.www.cookshoongauth.exeption.LoginValidationException;
import store.cookshoong.www.cookshoongauth.exeption.MissingRefreshTokenException;
import store.cookshoong.www.cookshoongauth.service.RefreshTokenValidationException;

/**
 * 인증과정에서 예외(회원 조회 실패, 비밀번호 검증실패)를 처리하는 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.13
 */
@ControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Void> apiCallException(HttpClientErrorException e) {
        return ResponseEntity.status(e.getStatusCode())
            .build();
    }

    @ExceptionHandler({LoginValidationException.class, RefreshTokenValidationException.class,
        InvalidTokenTypeException.class, MissingRequestHeaderException.class, InvalidAccountCodeException.class,
        MissingRefreshTokenException.class})
    public ResponseEntity<Void> badRequest(Exception e) {
        return ResponseEntity.badRequest()
            .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleInternalServerError(Exception e) {
        return ResponseEntity.internalServerError()
            .build();
    }
}
