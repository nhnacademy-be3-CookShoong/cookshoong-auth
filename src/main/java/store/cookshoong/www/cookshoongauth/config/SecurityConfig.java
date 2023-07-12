package store.cookshoong.www.cookshoongauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * {설명을 작성해주세요}
 *
 * @author koesnam (추만석)
 * @since 2023.07.12
 */
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .anyRequest()
            .permitAll();

        http.csrf()
            .disable();

        return http.build();
    }

}
