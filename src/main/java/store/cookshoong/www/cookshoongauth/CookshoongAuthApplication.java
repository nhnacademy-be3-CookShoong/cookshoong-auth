package store.cookshoong.www.cookshoongauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CookshoongAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookshoongAuthApplication.class, args);
    }

}
