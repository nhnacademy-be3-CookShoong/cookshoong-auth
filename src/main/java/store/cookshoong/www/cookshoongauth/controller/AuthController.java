package store.cookshoong.www.cookshoongauth.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
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

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponseDto> login(@RequestBody LoginRequestDto loginRequestDto)
        throws HttpClientErrorException {
        AccountInfo accountInfo = authService.executeAuthentication(loginRequestDto);
        return ResponseEntity.ok(authService.issueTokens(accountInfo));
    }
}
