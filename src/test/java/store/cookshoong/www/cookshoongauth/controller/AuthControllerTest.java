package store.cookshoong.www.cookshoongauth.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import store.cookshoong.www.cookshoongauth.adapter.ApiAdapter;
import store.cookshoong.www.cookshoongauth.config.SecurityConfig;
import store.cookshoong.www.cookshoongauth.jwt.JsonWebTokenProvider;
import store.cookshoong.www.cookshoongauth.jwt.JwtProperties;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.AuthenticationResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.LoginSuccessResponseDto;
import store.cookshoong.www.cookshoongauth.model.vo.AccountInfo;
import store.cookshoong.www.cookshoongauth.service.AuthService;

/**
 * 엔드포인트 동작 확인.
 *
 * @author koesnam (추만석)
 * @since 2023.07.15
 */
@WebMvcTest(AuthController.class)
@Import({JwtProperties.class, SecurityConfig.class})
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @Test
    @DisplayName("로그인 - 검증 성공일 때")
    @WithMockUser
    void login() throws Exception {
        // ISSUE: Body가 비어서 옴. (테스트환경에서만)
        LoginRequestDto testDto = ReflectionUtils.newInstance(LoginRequestDto.class);
        ReflectionTestUtils.setField(testDto, "loginId", "testUser1");
        ReflectionTestUtils.setField(testDto, "password", "1234");

        LoginSuccessResponseDto expect = new LoginSuccessResponseDto("accessToken", "refreshToken");
        AccountInfo testAccountInfo = mock(AccountInfo.class);
        doReturn(testAccountInfo).when(authService).executeAuthentication(testDto);
        doReturn(expect).when(authService).issueTokens(testAccountInfo);

        RequestBuilder request = MockMvcRequestBuilders.post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testDto));

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 - 검증 실패일 때")
    @WithMockUser
    void login2() throws Exception {
        LoginRequestDto testDto = ReflectionUtils.newInstance(LoginRequestDto.class);
        ReflectionTestUtils.setField(testDto, "loginId", "testUser1");
        ReflectionTestUtils.setField(testDto, "password", "12321532");

        doThrow(HttpClientErrorException.class).when(authService).executeAuthentication(any(LoginRequestDto.class));

        RequestBuilder request = MockMvcRequestBuilders.post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testDto));

        mockMvc.perform(request)
            .andExpect(status().isNotFound())
            .andDo(print());
    }
}
