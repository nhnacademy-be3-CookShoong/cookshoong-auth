package store.cookshoong.www.cookshoongauth.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * API 를 호출하기 위한 RestTemplate 에 대한 설정클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.13
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 기본 RestTemplate 을 Bean 으로 등록한다.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(10))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }
}
