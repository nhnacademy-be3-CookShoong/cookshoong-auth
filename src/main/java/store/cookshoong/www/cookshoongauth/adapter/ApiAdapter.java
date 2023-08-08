package store.cookshoong.www.cookshoongauth.adapter;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.AccountInfoResponseDto;
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
    private static final String ACCOUNT_API_PREFIX = "api/accounts";

    /**
     * 자격증명정보를 백엔드 서버에 호출한다.
     *
     * @param loginRequestDto the login request dto
     * @return the authentication response dto
     */
    public AuthenticationResponseDto fetchCredential(LoginRequestDto loginRequestDto) {
        URI uri = UriComponentsBuilder.fromUriString(apiProperties.getGatewayUri())
            .pathSegment(ACCOUNT_API_PREFIX)
            .pathSegment("{loginId}")
            .path("auth")
            .buildAndExpand(loginRequestDto.getLoginId())
            .toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, AuthenticationResponseDto.class)
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
            .pathSegment(ACCOUNT_API_PREFIX)
            .pathSegment("{accountId}")
            .pathSegment("status")
            .buildAndExpand(accountId)
            .toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, AccountStatusResponseDto.class)
            .getBody();
    }

    /**
     * API 서버에서 OAuth2로 로그인한 사용자 정보를 가져오는 메서드.
     *
     * @param provider    the provider
     * @param accountCode the account code
     * @return the account info response dto
     */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public AccountInfoResponseDto sendOAuthInfo(String provider, String accountCode) {
        URI uri = UriComponentsBuilder.fromUriString(apiProperties.getGatewayUri())
            .pathSegment(ACCOUNT_API_PREFIX)
            .pathSegment("oauth2")
            .queryParam("accountCode", accountCode)
            .queryParam("provider", provider)
            .build()
            .toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, AccountInfoResponseDto.class)
            .getBody();
    }

    /**
     * 마지막 로그인 날짜를 위해 로그인 성공을 Backend 서버에 알리는 메서드.
     *
     * @param accountId the account id
     */
    public void sendLoginSuccess(String accountId) {
        URI uri = UriComponentsBuilder.fromUriString(apiProperties.getGatewayUri())
            .pathSegment(ACCOUNT_API_PREFIX)
            .pathSegment("{accountId}")
            .pathSegment("auth-success")
            .buildAndExpand(accountId)
            .toUri();

        restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity.EMPTY, Void.class);
    }
}
