package store.cookshoong.www.cookshoongauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 애플리케이션 엔트리포인트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.03
 */
@ConfigurationPropertiesScan
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@SpringBootApplication
public class CookshoongAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookshoongAuthApplication.class, args);
    }

}
