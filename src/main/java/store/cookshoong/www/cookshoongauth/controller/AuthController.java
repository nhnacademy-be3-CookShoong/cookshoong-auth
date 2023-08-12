package store.cookshoong.www.cookshoongauth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import store.cookshoong.www.cookshoongauth.exeption.InvalidAccountCodeException;
import store.cookshoong.www.cookshoongauth.exeption.InvalidTokenTypeException;
import store.cookshoong.www.cookshoongauth.exeption.LoginValidationException;
import store.cookshoong.www.cookshoongauth.exeption.MissingRefreshTokenException;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.LoginSuccessResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.TokenReissueResponseDto;
import store.cookshoong.www.cookshoongauth.model.vo.AccountInfo;
import store.cookshoong.www.cookshoongauth.service.AuthService;

import javax.servlet.http.Cookie;
import javax.validation.Valid;

/**
 * 인증처리에 대한 엔드포인트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.13
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final int TOKEN_START_INDEX = 7;

    /**
     * 로그인 처리에 대한 엔드포인트.
     *
     * @param loginRequestDto the login request dto
     * @param bindingResult   the binding result
     * @return the response entity
     * @throws HttpClientErrorException the http client error exception
     */
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                                         BindingResult bindingResult)
        throws HttpClientErrorException {
        if (bindingResult.hasErrors()) {
            throw new LoginValidationException();
        }

        AccountInfo accountInfo = authService.executeAuthentication(loginRequestDto);
        return ResponseEntity.ok(authService.issueTokens(accountInfo));
    }

    /**
     * OAuth 로그인시 사용자 정보 조회를 위한 엔드포인트.
     *
     * @param accountCode the account code
     * @param provider    the provider
     * @return the response entity
     */
    @GetMapping("/login/oauth2")
    public ResponseEntity<LoginSuccessResponseDto> oauthLogin(@RequestHeader("X-Account-Code") String accountCode,
                                                              @RequestHeader("X-Provider") String provider) {
        if (!StringUtils.hasText(accountCode) || !StringUtils.hasText(provider)) {
            throw new InvalidAccountCodeException();
        }

        AccountInfo accountInfo = authService.fetchAccountInfo(provider, accountCode);
        return ResponseEntity.ok(authService.issueTokens(accountInfo));
    }

    /**
     * 리프레쉬 토큰을 재발급하는 엔드포인트.
     *
     * @param authorization the authorization
     * @return the response entity
     */
    @GetMapping("/reissue")
    public ResponseEntity<TokenReissueResponseDto> reissue(@RequestHeader("Authorization") String authorization,
                                                           @CookieValue("CRT") Cookie refreshToken) {
        if (!StringUtils.startsWithIgnoreCase(authorization, "Bearer ")) {
            throw new InvalidTokenTypeException(authorization.split(" ")[0]);
        }
        if (!StringUtils.hasText(refreshToken.getValue())) {
            throw new MissingRefreshTokenException();
        }
        return ResponseEntity.ok(authService.reissueToken(refreshToken.getValue()));
    }
}
