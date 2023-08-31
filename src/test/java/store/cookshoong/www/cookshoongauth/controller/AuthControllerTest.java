package store.cookshoong.www.cookshoongauth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import store.cookshoong.www.cookshoongauth.config.SecurityConfig;
import store.cookshoong.www.cookshoongauth.exeption.InvalidAccountCodeException;
import store.cookshoong.www.cookshoongauth.jwt.JwtValidator;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.AccountInfoResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.AuthenticationResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.LoginSuccessResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.TokenReissueResponseDto;
import store.cookshoong.www.cookshoongauth.model.vo.AccountInfo;
import store.cookshoong.www.cookshoongauth.service.AuthService;

/**
 * 엔드포인트 동작 확인.
 *
 * @author koesnam (추만석)
 * @since 2023.07.15
 */
@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class})
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;
    @MockBean
    JwtValidator jwtValidator;

    @Test
    @DisplayName("로그인 - 검증 성공일 때")
    void login() throws Exception {
        LoginRequestDto testDto = ReflectionUtils.newInstance(LoginRequestDto.class);
        ReflectionTestUtils.setField(testDto, "loginId", "testUser1");
        ReflectionTestUtils.setField(testDto, "password", "1234");

        AuthenticationResponseDto testResponseDto = mock(AuthenticationResponseDto.class);
        when(testResponseDto.getLoginId()).thenReturn("testUser1");
        when(testResponseDto.getAttributes()).thenReturn(Map.of("authority", "CUSTOMER"));
        when(testResponseDto.getPassword()).thenReturn("1234");
        AccountInfo testAccountInfo = new AccountInfo(testResponseDto);

        LoginSuccessResponseDto expect = new LoginSuccessResponseDto("accessToken", "refreshToken");

        doReturn(testAccountInfo).when(authService).executeAuthentication(any(LoginRequestDto.class));
        doReturn(expect).when(authService).issueTokens(testAccountInfo);

        RequestBuilder request = MockMvcRequestBuilders.post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testDto));

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value(expect.getAccessToken()))
            .andExpect(jsonPath("$.refreshToken").value(expect.getRefreshToken()))
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 - 아이디 또는 비밀번호가 불일치할 경우")
    void login_2() throws Exception {
        LoginRequestDto testDto = ReflectionUtils.newInstance(LoginRequestDto.class);
        ReflectionTestUtils.setField(testDto, "loginId", "testUser1");
        ReflectionTestUtils.setField(testDto, "password", "12321532");

        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(authService)
            .executeAuthentication(any(LoginRequestDto.class));

        RequestBuilder request = MockMvcRequestBuilders.post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testDto));

        mockMvc.perform(request)
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 - 로그인 요청값에서 필수값 누락 - 비밀번호 누락")
    void login_3() throws Exception {
        LoginRequestDto testDto = ReflectionUtils.newInstance(LoginRequestDto.class);
        ReflectionTestUtils.setField(testDto, "loginId", "testUser1");

        RequestBuilder request = MockMvcRequestBuilders.post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testDto));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 - 로그인 요청값에서 필수값 누락 - 아이디 누락")
    void login_4() throws Exception {
        LoginRequestDto testDto = ReflectionUtils.newInstance(LoginRequestDto.class);
        ReflectionTestUtils.setField(testDto, "password", "12321532");

        RequestBuilder request = MockMvcRequestBuilders.post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testDto));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("재발급 - 헤더에 제대로 된 토큰값이 들어온 경우")
    void reissue() throws Exception {
        TokenReissueResponseDto expect = new TokenReissueResponseDto("accessToken",
            "refreshToken");
        doReturn(expect).when(authService).reissueToken(anyString());
        doReturn(true).when(jwtValidator).validatePairToken(anyString(), anyString());

        RequestBuilder request = MockMvcRequestBuilders.get("/auth/reissue")
            .contentType(MediaType.APPLICATION_JSON)
            .cookie(new Cookie("CRT", "refreshToken"))
            .header("Authorization", "Bearer " + "ValidToken");

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value(expect.getAccessToken()))
            .andExpect(jsonPath("$.refreshToken").value(expect.getRefreshToken()))
            .andDo(print());
    }

    @DisplayName("재발급 - 헤더에 Bearer 타입이 아닌 값이 들어온 경우")
    @ParameterizedTest
    @ValueSource(strings = {"", "Basic ", "Digest ", "HOBA ", "Mutual ", "AWS4-HMAC-SHA256 ", "asdqwg"})
    void reissue_2(String header) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/auth/reissue")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", header + "ValidToken")
            .cookie(new Cookie("CRT", "refreshToken"));

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("재발급 - 두 토큰값의 토큰 식별자가 다르게 들어오는 경우")
    void reissue_3() throws Exception {
        doReturn(false).when(jwtValidator).validatePairToken(anyString(), anyString());

        RequestBuilder request = MockMvcRequestBuilders.get("/auth/reissue")
            .contentType(MediaType.APPLICATION_JSON)
            .cookie(new Cookie("CRT", "refreshToken"))
            .header("Authorization", "Bearer " + "InvalidToken");

        MvcResult result = mockMvc.perform(request)
            .andExpect(status().isForbidden())
            .andReturn();

        assertThat(result.getResponse().getContentAsString()).isBlank();
    }

    @ParameterizedTest
    @DisplayName("OAuth2 로그인 - 검증 성공일 때")
    @ValueSource(strings = {"payco", "Payco"})
    void oauthLogin(String provider) throws Exception {
        AccountInfoResponseDto testDto = ReflectionUtils.newInstance(AccountInfoResponseDto.class);
        ReflectionTestUtils.setField(testDto, "accountId", "1");
        ReflectionTestUtils.setField(testDto, "loginId", "tempUser");
        ReflectionTestUtils.setField(testDto, "authority", "CUSTOMER");
        ReflectionTestUtils.setField(testDto, "status", "ACTIVE");
        AccountInfo testAccountInfo = new AccountInfo(testDto);
        LoginSuccessResponseDto expect = new LoginSuccessResponseDto("accessToken",
            "refreshToken");

        String accountCode = "valid-accountCode";

        RequestBuilder request = MockMvcRequestBuilders.get("/auth/login/oauth2")
            .header("X-Account-Code", accountCode)
            .header("X-Provider", provider);

        when(authService.fetchAccountInfo(provider, accountCode)).thenReturn(testAccountInfo);
        when(authService.issueTokens(testAccountInfo)).thenReturn(expect);

        mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value(expect.getAccessToken()))
            .andExpect(jsonPath("$.refreshToken").value(expect.getRefreshToken()));
    }

    @ParameterizedTest
    @DisplayName("OAuth2 로그인 - 필수값 누락 - 회원식별자 누락")
    @ValueSource(strings = {"      ", ""})
    void oauthLogin_2(String accountCode) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/auth/login/oauth2")
            .header("X-Account-Code", accountCode)
            .header("X-Provider", "payco");

        mockMvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(InvalidAccountCodeException.class, result.getResolvedException()));
    }

    @ParameterizedTest
    @DisplayName("OAuth2 로그인 - 필수값 누락 - 회원식별자 누락")
    @ValueSource(strings = {"      ", ""})
    void oauthLogin_3(String provider) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/auth/login/oauth2")
            .header("X-Account-Code", "valid-account-code")
            .header("X-Provider", provider);

        mockMvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(InvalidAccountCodeException.class, result.getResolvedException()));
    }
}
