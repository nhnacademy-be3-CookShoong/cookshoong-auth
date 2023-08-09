package store.cookshoong.www.cookshoongauth.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로그인 성공시 로깅을 위한 포인트컷용 애노테이션.
 *
 * @author koesnam (추만석)
 * @since 2023.08.07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginProcess {
}
