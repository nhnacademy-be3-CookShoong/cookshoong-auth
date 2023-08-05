package store.cookshoong.www.cookshoongauth.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Duration;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import store.cookshoong.www.cookshoongauth.interceptor.RestTemplateHeaderModifierInterceptor;
import store.cookshoong.www.cookshoongauth.jwt.JsonWebTokenProvider;
import store.cookshoong.www.cookshoongauth.skm.SecureKeyManagerProperties;

/**
 * API 를 호출하기 위한 RestTemplate 에 대한 설정클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.13
 */
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    /**
     * 기본 RestTemplate 을 Bean 으로 등록한다.
     *
     * @return the rest template
     */
    @Bean
    @ConditionalOnBean(JsonWebTokenProvider.class)
    public RestTemplate restTemplate(JsonWebTokenProvider jwtProvider) {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(10))
            .setReadTimeout(Duration.ofSeconds(10))
            .additionalInterceptors(new RestTemplateHeaderModifierInterceptor(jwtProvider))
            .build();
    }

    /**
     * 클라이언트 인증서를 담은 RestTemplate.
     * (사용시 필드명을 sslRestTemplate 으로 해줘야 주입된다.)
     *
     * @param properties the properties
     * @return the rest template
     * @throws KeyStoreException         the key store exception
     * @throws CertificateException      the certificate exception
     * @throws IOException               the io exception
     * @throws NoSuchAlgorithmException  the no such algorithm exception
     * @throws UnrecoverableKeyException the unrecoverable key exception
     * @throws KeyManagementException    the key management exception
     */
    @Bean(name = "sslRestTemplate")
    public RestTemplate sslRestTemplate(SecureKeyManagerProperties properties) throws KeyStoreException,
        CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        String password = properties.getPassword();

        RestTemplate sslRestTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();

        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        InputStream inputStream = new ClassPathResource("CookShoong.p12").getInputStream();
        clientStore.load(inputStream, password.toCharArray());
        SSLContext sslContext = SSLContextBuilder.create()
            .setProtocol("TLS")
            .loadKeyMaterial(clientStore, password.toCharArray())
            .loadTrustMaterial(new TrustSelfSignedStrategy())
            .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(sslConnectionSocketFactory)
            .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        sslRestTemplate.setRequestFactory(requestFactory);

        return sslRestTemplate;
    }
}
