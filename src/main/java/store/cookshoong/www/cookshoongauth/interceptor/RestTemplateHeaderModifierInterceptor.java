package store.cookshoong.www.cookshoongauth.interceptor;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import store.cookshoong.www.cookshoongauth.jwt.JsonWebTokenProvider;

/**
 * RestTemplate 을 통해 API 를 호출하기전 인증 헤더를 달아주기 위한 인터셉터.
 *
 * @author koesnam (추만석)
 * @since 2023.07.24
 */
@RequiredArgsConstructor
public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {
    private final JsonWebTokenProvider jwtProvider;

    /**
     * RestTemplate 을 통해 API 를 호출하기 전 인증 헤더를 추가해준다.
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {
        String jti = UUID.randomUUID().toString();
        request.getHeaders().setBearerAuth(jwtProvider.createAccessToken(jti, "AUTH-SERVER"));
        return execution.execute(request, body);
    }
}
