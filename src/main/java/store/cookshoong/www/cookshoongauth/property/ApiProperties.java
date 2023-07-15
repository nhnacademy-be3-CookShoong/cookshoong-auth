package store.cookshoong.www.cookshoongauth.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * API 를 호출할 주소를 담고 있는 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.15
 */
@Getter
@Setter
@ConfigurationProperties("api")
public class ApiProperties {
    private String gatewayUri;
}
