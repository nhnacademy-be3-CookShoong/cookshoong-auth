package store.cookshoong.www.cookshoongauth.jwt;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt 관련 설정값을 가져오기 위한 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */
@Configuration
@ConfigurationPropertiesScan(basePackageClasses = JwtConfig.class)
public class JwtConfig {
}
