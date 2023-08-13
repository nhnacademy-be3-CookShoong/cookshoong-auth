package store.cookshoong.www.cookshoongauth.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import store.cookshoong.www.cookshoongauth.adapter.ApiAdapter;
import store.cookshoong.www.cookshoongauth.model.vo.AccountIdAware;

/**
 * 로그인 성공시 마지막 로그인 기록을 하기 위한 Aspect.
 *
 * @author koesnam (추만석)
 * @since 2023.08.07
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {
    private final ApiAdapter apiAdapter;

    @AfterReturning(pointcut = "@annotation(LoginProcess)", returning = "accountInfo")
    public void logLoginSuccess(AccountIdAware accountInfo) {
        apiAdapter.sendLoginSuccess(accountInfo.getAccountId());
    }
}
