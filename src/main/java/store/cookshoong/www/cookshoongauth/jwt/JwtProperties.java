package store.cookshoong.www.cookshoongauth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt 관련 설정값들을 담고 있는 클래스
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */
@Getter
@Setter
@ConfigurationProperties("jwt")
public class JwtProperties {
    // TODO: secret을 SKM에 올리기.
    private String secret;
    private String refreshSecret;
    private Integer accessTokenTtl;
    private Integer refreshTokenTtl;
}
