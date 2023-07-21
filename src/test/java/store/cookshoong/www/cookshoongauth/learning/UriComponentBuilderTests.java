package store.cookshoong.www.cookshoongauth.learning;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * UriComponentsBuilder 의 동작을 알아보기 위한 테스트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.15
 */
class UriComponentBuilderTests {

    @Test
    @DisplayName("path 만을 사용하면 /가 붙지 않는다.")
    void test1() {
        String expect = "http://localhost:8080/apiaccounts{loginId}auth";

        String actual = UriComponentsBuilder.fromUriString("http://localhost:8080")
            .path("api")
            .path("accounts")
            .path("{loginId}")
            .path("auth")
            .build()
            .toString();

        System.out.println(actual);
        assertThat(actual, is(expect));
    }

    @Test
    @DisplayName("pathSegment로 값을 넣어주면 앞뒤로 /가 붙는다.")
    void test2() {
        String expect = "http://localhost:8080/api/accounts/{loginId}/auth";

        String actual = UriComponentsBuilder.fromUriString("http://localhost:8080")
            .path("api")
            .pathSegment("accounts")
            .path("{loginId}")
            .pathSegment("auth")
            .build()
            .toString();

        System.out.println(actual);
        assertThat(actual, is(expect));
    }

    @Test
    @DisplayName("pathSegment가 앞뒤로 /를 넣는다고 해서 중복되는 것은 아니다.")
    void test3() {
        String expect = "http://localhost:8080/api/accounts/{loginId}/auth";

        String actual = UriComponentsBuilder.fromUriString("http://localhost:8080")
            .pathSegment("api")
            .pathSegment("accounts")
            .pathSegment("{loginId}")
            .pathSegment("auth")
            .build()
            .toString();

        System.out.println(actual);
        assertThat(actual, is(expect));
    }

    @Test
    @DisplayName("pathSegment도 뒤에 path가 유효하지 않으면(빈값) /를 넣지 않는다.")
    void test4() {
        String expect = "http://localhost:8080/api/accounts/{loginId}/auth";

        String actual = UriComponentsBuilder.fromUriString("http://localhost:8080")
            .pathSegment("api")
            .pathSegment("accounts")
            .pathSegment("{loginId}")
            .pathSegment("auth")
            .pathSegment("")
            .build()
            .toString();

        System.out.println(actual);
        assertThat(actual, is(expect));
    }

    @Test
    @DisplayName("pathSegment도 뒤에 path가 유효하지 않으면(공백) /를 넣지 않는다.")
    void test5() {
        String expect = "http://localhost:8080/api/accounts/{loginId}/auth";

        String actual = UriComponentsBuilder.fromUriString("http://localhost:8080")
            .pathSegment("api")
            .pathSegment("accounts")
            .pathSegment("{loginId}")
            .pathSegment("auth")
            .pathSegment("            ")
            .build()
            .toString();

        System.out.println(actual);
        assertThat(actual, is(expect));
    }

    @Test
    @DisplayName("path을 사용하려면 직접 중간에 / 붙여야 한다.")
    void test6() {
        String expect = "http://localhost:8080/api/accounts/{loginId}/auth";

        String actual = UriComponentsBuilder.fromUriString("http://localhost:8080")
            .path("api/accounts/{loginId}/auth")
            .build()
            .toString();

        System.out.println(actual);
        assertThat(actual, is(expect));
    }

    @Test
    @DisplayName("path을 여러번 사용하면 root uri 뒤에만 /가 붙고 나머지는 붙지 않는다.")
    void test7() {
        String expect = "http://localhost:8080/api/accounts/{loginId}/auth/tail";

        String actual = UriComponentsBuilder.fromUriString("http://localhost:8080")
            .path("api/accounts/{loginId}/auth")
            .path("tail")
            .build()
            .toString();

        System.out.println(actual);
        assertThat(actual, is(not(expect)));
    }
}
