package store.cookshoong.www.cookshoongauth.adapter;

import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import store.cookshoong.www.cookshoongauth.jwt.JsonWebTokenProvider;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.AccountStatusResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.AuthenticationResponseDto;
import store.cookshoong.www.cookshoongauth.property.ApiProperties;

/**
 * Resource Server(Backend server)로 부터 인증 정보를 가져오기 위한 어댑터.
 *
 * @author koesnam (추만석)
 * @since 2023.07.15
 */
@Component
@RequiredArgsConstructor
public class ApiAdapter {
    private final RestTemplate restTemplate;
    private final ApiProperties apiProperties;
    private final JsonWebTokenProvider jwtProvider;

    /**
     * 자격증명정보를 백엔드 서버에 호출한다.
     *
     * @param loginRequestDto the login request dto
     * @return the authentication response dto
     */
    public AuthenticationResponseDto fetchCredential(LoginRequestDto loginRequestDto) {
        URI uri = UriComponentsBuilder.fromUriString(apiProperties.getGatewayUri())
            .pathSegment("api")
            .pathSegment("accounts")
            .pathSegment("{loginId}")
            .path("auth")
            .buildAndExpand(loginRequestDto.getLoginId())
            .toUri();

        HttpEntity<Void> request = new HttpEntity<>(getAuthorizedHeader());

        return restTemplate.exchange(uri, HttpMethod.GET, request, AuthenticationResponseDto.class)
            .getBody();
    }


    /**
     * 회원의 현재상태를 가져온다.
     *
     * @param accountId the account id
     * @return the account status response dto
     */
    public AccountStatusResponseDto fetchAccountStatus(Long accountId) {
        URI uri = UriComponentsBuilder.fromUriString(apiProperties.getGatewayUri())
            .pathSegment("api")
            .pathSegment("accounts")
            .pathSegment("{accountId}")
            .pathSegment("status")
            .buildAndExpand(accountId)
            .toUri();

        HttpEntity<Void> request = new HttpEntity<>(getAuthorizedHeader());

        return restTemplate.exchange(uri, HttpMethod.GET, request, AccountStatusResponseDto.class)
            .getBody();
    }

    /**
     * API 서버와 통신하기 위한 인증 헤더.
     *
     * @return the authorized header
     */
    public HttpHeaders getAuthorizedHeader() {
        HttpHeaders authorizedHeader = new HttpHeaders();
        String jti = UUID.randomUUID().toString();
        authorizedHeader.setBearerAuth(jwtProvider.createAccessToken(jti, "AUTH-SERVER"));
        return authorizedHeader;
    }
}
