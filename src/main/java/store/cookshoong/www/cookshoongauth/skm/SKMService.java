package store.cookshoong.www.cookshoongauth.skm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Secure Key Manager 서비스를 호출하기 위한 서비스 클래스.
 *
 * @author koesnam
 * @since 2023.07.11
 */
//@Profile("!default")
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Service
@RequiredArgsConstructor
public class SKMService {
    private final RestTemplate sslRestTemplate;
    private final SecureKeyManagerProperties secureKeyManagerProperties;

    /**
     * SKM API 호출을 통해 저장해둔 키값들을 가져온다.
     *
     * @param <T>       저장해둔 키값과 형식이 맞는 타입
     * @param keyid     SKM 저장되있는 기밀 데이터의 아이디
     * @param valueType 저장해둔 키값과 형식이 맞는 타입
     * @return 키값
     * @throws JsonProcessingException the json processing exception
     */
    public <T> T fetchSecrets(String keyid, Class<T> valueType) throws JsonProcessingException {
        String appkey = secureKeyManagerProperties.getAppkey();

        URI secretUri = SecureKeyManagerUri.getSecretUri(appkey, keyid);

        String response = Objects.requireNonNull(sslRestTemplate
                .getForEntity(secretUri, SecureKeyManagerResponseDto.class).getBody()
            )
            .getResponseBody()
            .getSecrets();

        return extractSecrets(response, valueType);
    }

    /**
     * SKM 에 JSON 형식으로 값을 저장해뒀을 때, 제대로 값이 매핑되지 않아 String -> 원하는 타입으로 매핑하기 위한 메서드.
     */
    private <T> T extractSecrets(String response, Class<T> valueType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, valueType);
    }

    /**
     * SKM API 호출 주소들이 담긴 클래스.
     *
     * @author koesnam (추만석)
     * @since 2023.07.12
     */
    private static class SecureKeyManagerUri {
        private static final String SCHEME = "https";
        private static final String HOST = "api-keymanager.nhncloudservice.com";
        private static final String APP_NAME = "keymanager";
        private static final String VERSION = "v1.0";

        private SecureKeyManagerUri() {}

        /**
         * 입력한 버전의 appkey 와 keyid 를 통해 기밀 데이터를 얻어오는 API 호출을 위한 URL.
         * (v1.0 API 호출)
         *
         * @param appkey Secure Manager Key 에 등록한 그룹이 가지는 appkey
         * @param keyid  the keyid 기밀 데이터의 아이디
         * @return 기밀데이터 호출 URL
         */
        private static URI getSecretUri(String appkey, String keyid) {
            return UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .pathSegment(APP_NAME)
                .pathSegment(VERSION)
                .pathSegment("appkey")
                .path("{appkey}")
                .pathSegment("secrets")
                .path("{keyid}")
                .buildAndExpand(appkey, keyid)
                .toUri();
        }
    }
}
