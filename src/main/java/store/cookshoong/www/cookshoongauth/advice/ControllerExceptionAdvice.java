package store.cookshoong.www.cookshoongauth.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

/**
 * 인증과정에서 예외(회원 조회 실패, 비밀번호 검증실패)를 처리하는 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.13
 */
@ControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Void> notfound() {
        return ResponseEntity.notFound()
            .build();
    }
}
