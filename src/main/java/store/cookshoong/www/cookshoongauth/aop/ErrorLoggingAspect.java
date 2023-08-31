package store.cookshoong.www.cookshoongauth.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 서버에서 처리된 예외들을 로깅하기 위한 Aspect.
 *
 * @author koesnam (추만석)
 * @since 2023.08.22
 */
@Slf4j
@Aspect
public class ErrorLoggingAspect {
    /**
     * 에러를 로깅하기 위한 어드바이스.
     *
     * @param joinPoint the join point
     */
    @Before("@within(org.springframework.web.bind.annotation.ControllerAdvice)")
    public void logError(JoinPoint joinPoint) {
        if (joinPoint.getArgs()[0] instanceof Exception) {
            Exception exception = (Exception) joinPoint.getArgs()[0];
            log.warn("처리 완료된 예외 : {} \n --> {}", exception.getMessage(), ExceptionUtils.getStackTrace(exception));
        }
    }
}
