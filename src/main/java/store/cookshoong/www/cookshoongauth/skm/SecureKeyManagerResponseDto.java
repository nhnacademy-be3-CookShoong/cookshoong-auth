package store.cookshoong.www.cookshoongauth.skm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Secure Key Manager 로 부터 시크릿 정보를 가져오기 위한 DTO.
 *
 * @author koesnam
 * @since 2023.07.10
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecureKeyManagerResponseDto {
    @JsonProperty("body")
    private SecureKeyManagerResponseBody responseBody;

    /**
     * ex)
     * "header" : {
     *  ...
     * },
     * "body" : {
     *     "secret" : { ... }
     * }
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SecureKeyManagerResponseBody {
        /**
         * ex)
         * "secret" : {
         *      "key1" : "",
         *      "key2" : "",
         *      ...
         *     }
         * }
         */
        @JsonProperty("secret")
        private String secrets;
    }
}



