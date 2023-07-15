package store.cookshoong.www.cookshoongauth.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import store.cookshoong.www.cookshoongauth.adapter.ApiAdapter;
import store.cookshoong.www.cookshoongauth.jwt.JsonWebTokenProvider;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.AuthenticationResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.LoginSuccessResponseDto;
import store.cookshoong.www.cookshoongauth.model.vo.AccountInfo;

/**
 * 인증처리를 목적으로 하는 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final ApiAdapter apiAdapter;
    private final PasswordEncoder passwordEncoder;
    private final JsonWebTokenProvider jwtProvider;

    /**
     * 인증 처리하는 메서드.
     *
     * @param loginRequestDto the login request dto
     * @return the account info
     */
    public AccountInfo executeAuthentication(LoginRequestDto loginRequestDto) {
        AuthenticationResponseDto credential = apiAdapter.fetchCredential(loginRequestDto);

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), credential.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        return new AccountInfo(credential);
    }

    /**
     * 토큰(액세스, 리프레쉬)에 회원정보를 담아서 발행해주는 메서드.
     *
     * @param accountInfo the account info
     * @return the login success response dto
     */
    public LoginSuccessResponseDto issueTokens(AccountInfo accountInfo) {
        String loginId = accountInfo.getLoginId();
        String authority = accountInfo.getAuthority();
        String status = accountInfo.getStatus();
        String accountId = accountInfo.getAccountId();

        String jti = UUID.randomUUID().toString();
        String accessToken = jwtProvider.createAccessToken(jti, authority);
        String refreshToken = jwtProvider.createRefreshToken(jti, accountId, status, loginId);

        saveRefreshToken(jti, refreshToken);
        return new LoginSuccessResponseDto(accessToken, refreshToken);
    }

    public void saveRefreshToken(String jti, String refreshToken) {
        // TODO: redis에 RefreshToken 저장.
    }
}
