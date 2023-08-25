package store.cookshoong.www.cookshoongauth.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import store.cookshoong.www.cookshoongauth.adapter.ApiAdapter;
import store.cookshoong.www.cookshoongauth.aop.LoginProcess;
import store.cookshoong.www.cookshoongauth.entity.RefreshToken;
import store.cookshoong.www.cookshoongauth.jwt.JsonWebTokenProvider;
import store.cookshoong.www.cookshoongauth.jwt.JwtResolver;
import store.cookshoong.www.cookshoongauth.model.request.LoginRequestDto;
import store.cookshoong.www.cookshoongauth.model.response.AccountInfoResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.AuthenticationResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.LoginSuccessResponseDto;
import store.cookshoong.www.cookshoongauth.model.response.TokenReissueResponseDto;
import store.cookshoong.www.cookshoongauth.model.vo.AccountInfo;
import store.cookshoong.www.cookshoongauth.model.vo.ParsedRefreshToken;
import store.cookshoong.www.cookshoongauth.repository.RefreshTokenRepository;

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
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 인증 처리하는 메서드.
     *
     * @param loginRequestDto the login request dto
     * @return the account info
     */
    @LoginProcess
    public AccountInfo executeAuthentication(LoginRequestDto loginRequestDto) {
        AuthenticationResponseDto credential = apiAdapter.fetchCredential(loginRequestDto);

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), credential.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        return new AccountInfo(credential);
    }

    /**
     * OAuth2로 로그인한 사용자의 정보를 가져오는 메서드.
     *
     * @param provider    the provider
     * @param accountCode the account code
     * @return the account info
     */
    @LoginProcess
    public AccountInfo fetchAccountInfo(String provider, String accountCode) {
        AccountInfoResponseDto response = apiAdapter.sendOAuthInfo(provider, accountCode);

        return new AccountInfo(response);
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
        String refreshToken = jwtProvider.createRefreshToken(jti, accountId, status, loginId, authority);

        // TODO : AOP로 빼내보기. 생성할 때마다 이전에 있던 거 지우고 저장하게끔
        saveRefreshToken(refreshToken);
        return new LoginSuccessResponseDto(accessToken, refreshToken);
    }

    /**
     * 기존의 리프레쉬 토큰을 기반으로 새로운 액세스토큰과, 리프레쉬 토큰을 발급한다.
     *
     * @param oldToken the old token
     * @return the token reissue response dto
     */
    public TokenReissueResponseDto reissueToken(String oldToken) {
        ParsedRefreshToken oldParsedToken = JwtResolver.resolveRefreshToken(oldToken);
        if (oldParsedToken.getAccountId() == null || oldParsedToken.getStatus() == null
            || oldParsedToken.getLoginId() == null) {
            throw new RefreshTokenValidationException();
        }

        refreshTokenRepository.deleteById(oldParsedToken.getJti());

        String currentStatus = apiAdapter.fetchAccountStatus(oldParsedToken.getAccountId()).getStatus();

        String newJti = UUID.randomUUID().toString();
        String newAccessToken = jwtProvider.createAccessToken(newJti, oldParsedToken.getAuthority());
        String newRefreshToken = jwtProvider.createRefreshToken(newJti, String.valueOf(oldParsedToken.getAccountId()),
            currentStatus, oldParsedToken.getLoginId(), oldParsedToken.getAuthority());

        saveRefreshToken(newRefreshToken);
        return new TokenReissueResponseDto(newAccessToken, newRefreshToken);
    }

    /**
     * 리프레쉬토큰을 레디스에 저장한다.
     *
     * @param refreshToken the refresh token
     */
    public void saveRefreshToken(String refreshToken) {
        refreshTokenRepository.save(RefreshToken.createRefreshToken(refreshToken));
    }
}
