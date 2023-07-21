package store.cookshoong.www.cookshoongauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import store.cookshoong.www.cookshoongauth.adapter.ApiAdapter;
import store.cookshoong.www.cookshoongauth.jwt.JsonWebTokenProvider;
import store.cookshoong.www.cookshoongauth.jwt.JwtProperties;
import store.cookshoong.www.cookshoongauth.jwt.JwtSecret;
import store.cookshoong.www.cookshoongauth.jwt.JwtTtl;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.AccountStatusResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.AuthenticationResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.LoginSuccessResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.TokenReissueResponseDto;
import store.cookshoong.www.cookshoongauth.model.vo.AccountInfo;
import store.cookshoong.www.cookshoongauth.repository.RefreshTokenRepository;

/**
 * AuthService 에 대한 테스트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.21
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    ApiAdapter apiAdapter;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Spy
    static JsonWebTokenProvider jwtProvider = new JsonWebTokenProvider(mock(JwtProperties.class));
    @InjectMocks
    AuthService authService;

    @BeforeAll
    static void setup() {
        JwtSecret testSecret = new JwtSecret("2589a135-d383-4a96-9282-91dff05586c7",
            "3506abec-f527-468c-83cd-62420189de1d");
        JwtTtl testTtl = new JwtTtl(3000L, 30000L);
        JwtProperties testProperties = ReflectionUtils.newInstance(JwtProperties.class, testSecret, testTtl);
        jwtProvider = new JsonWebTokenProvider(testProperties);
    }

    @Test
    @DisplayName("인증 처리 - 비밀번호 일치")
    void executeAuthentication() {
        LoginRequestDto testRequestDto = ReflectionUtils.newInstance(LoginRequestDto.class);
        ReflectionTestUtils.setField(testRequestDto, "loginId", "user1");
        ReflectionTestUtils.setField(testRequestDto, "password", "1234");

        AuthenticationResponseDto expect = ReflectionUtils.newInstance(AuthenticationResponseDto.class);
        ReflectionTestUtils.setField(expect, "loginId", "user1");
        ReflectionTestUtils.setField(expect, "password", "1234");
        ReflectionTestUtils.setField(expect, "attributes",
            Map.of("authority", "CUSTOMER", "accountId", "1", "status", "ACTIVE"));

        doReturn(expect).when(apiAdapter).fetchCredential(any(LoginRequestDto.class));
        doReturn(true).when(passwordEncoder).matches(testRequestDto.getPassword(), expect.getPassword());


        AccountInfo actual = authService.executeAuthentication(testRequestDto);

        assertAll(
            () -> assertThat(actual.getLoginId()).isEqualTo(expect.getLoginId()),
            () -> assertThat(actual.getAuthority()).isEqualTo(expect.getAttributes().get("authority")),
            () -> assertThat(actual.getStatus()).isEqualTo(expect.getAttributes().get("status")),
            () -> assertThat(actual.getAccountId()).isEqualTo(expect.getAttributes().get("accountId"))
        );
    }

    @Test
    @DisplayName("인증 처리 - 비밀번호 불일치")
    void executeAuthentication_2() {
        LoginRequestDto testRequestDto = ReflectionUtils.newInstance(LoginRequestDto.class);
        ReflectionTestUtils.setField(testRequestDto, "loginId", "user1");
        ReflectionTestUtils.setField(testRequestDto, "password", "1234");

        AuthenticationResponseDto expect = ReflectionUtils.newInstance(AuthenticationResponseDto.class);
        ReflectionTestUtils.setField(expect, "loginId", "user1");
        ReflectionTestUtils.setField(expect, "password", "34645");
        ReflectionTestUtils.setField(expect, "attributes",
            Map.of("authority", "CUSTOMER", "accountId", "1", "status", "ACTIVE"));

        doReturn(expect).when(apiAdapter).fetchCredential(any(LoginRequestDto.class));
        doReturn(false).when(passwordEncoder).matches(testRequestDto.getPassword(), expect.getPassword());

        assertThatThrownBy(() -> authService.executeAuthentication(testRequestDto))
            .isInstanceOf(HttpClientErrorException.class);
    }

    @Test
    @DisplayName("토큰 발급 - 성공")
    void issueTokens() {
        AuthenticationResponseDto testResponseDto = ReflectionUtils.newInstance(AuthenticationResponseDto.class);
        ReflectionTestUtils.setField(testResponseDto, "loginId", "user1");
        ReflectionTestUtils.setField(testResponseDto, "password", "1234");
        ReflectionTestUtils.setField(testResponseDto, "attributes",
            Map.of("authority", "CUSTOMER", "accountId", "1", "status", "ACTIVE"));

        AccountInfo testInfo = new AccountInfo(testResponseDto);

        LoginSuccessResponseDto actual = authService.issueTokens(testInfo);

        assertAll(
            () -> assertThat(actual.getAccessToken()).isNotBlank(),
            () -> assertThat(actual.getRefreshToken()).isNotBlank()
        );
    }

    @Test
    @DisplayName("토큰 재발급 - 리프레쉬 토큰이 아닌 액세스 토큰이 들어온 경우")
    void reissueToken() {
        String testAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOiIxMjM0NTY3ODkwIiwiYXV0aG9yaXR5IjoiSm9obiBEb2UiLCJqdGkiOjE1MTYyMzkwMjJ9.-vVrXoOCoJfrXk9uxNWvk-_VEGJdVkAvWbY0V7W8Vs8";

        assertThatThrownBy(
            () -> authService.reissueToken(testAccessToken)
        ).isInstanceOf(RefreshTokenValidationException.class);
    }

    @Test
    @DisplayName("토큰 재발급 - 정상적인 리프레쉬 토큰이 들어온 경우")
    void saveRefreshToken() {
        String testRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOiIxMjM0NTY3ODkwIiwiYXV0aG9yaXR5IjoiSm9obiBEb2UiLCJqdGkiOjE1MTYyMzkwMjIsInN0YXR1cyI6IkFDVElWRSIsImxvZ2luSWQiOiJ1c2VyMSJ9.ZJstgOF8XelgeB3MitqNNwByuloZLZeFI_TBrAuSWow";
        AccountStatusResponseDto testResponseDto = ReflectionUtils.newInstance(AccountStatusResponseDto.class);
        ReflectionTestUtils.setField(testResponseDto, "status", "ACTIVE");

        doReturn(testResponseDto).when(apiAdapter).fetchAccountStatus(anyLong());

        TokenReissueResponseDto actual = authService.reissueToken(testRefreshToken);

        assertAll(
            () -> assertThat(actual.getAccessToken()).isNotBlank(),
            () -> assertThat(actual.getRefreshToken()).isNotBlank()
        );
    }
}
