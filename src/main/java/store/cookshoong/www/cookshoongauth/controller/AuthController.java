package store.cookshoong.www.cookshoongauth.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import store.cookshoong.www.cookshoongauth.exeption.LoginValidationException;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.LoginSuccessResponseDto;
import store.cookshoong.www.cookshoongauth.model.vo.AccountInfo;
import store.cookshoong.www.cookshoongauth.service.AuthService;

/**
 * 인증처리에 대한 엔드포인트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.13
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

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
}
