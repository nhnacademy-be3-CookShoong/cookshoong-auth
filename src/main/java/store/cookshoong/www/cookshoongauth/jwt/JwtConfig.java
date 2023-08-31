package store.cookshoong.www.cookshoongauth.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.cookshoong.www.cookshoongauth.skm.SKMService;
import store.cookshoong.www.cookshoongauth.util.Times;

/**
 * Jwt 관련 설정값을 가져오기 위한 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = JwtConfig.class)
public class JwtConfig {

    /**
     * Jwt 의 서명키값, 유효기간을 설정하는 빈.
     *
     * @param jwtKeyid   the jwt keyid
     * @param skmService the skm service
     * @return the jwt properties
     * @throws JsonProcessingException the json processing exception
     */
    @Bean
    public JwtProperties jwtProperties(@Value("${cookshoong.skm.keyid.jwt}") String jwtKeyid,
                                       SKMService skmService) throws JsonProcessingException {
        JwtSecret jwtSecret = skmService.fetchSecrets(jwtKeyid, JwtSecret.class);
        JwtTtl jwtTtl = new JwtTtl(30 * Times.MINUTE,  2 * Times.HOUR);
        return new JwtProperties(jwtSecret, jwtTtl);

    }
}
