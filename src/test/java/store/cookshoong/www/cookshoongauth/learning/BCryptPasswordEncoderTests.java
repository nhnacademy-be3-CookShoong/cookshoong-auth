package store.cookshoong.www.cookshoongauth.learning;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCryptPasswordEncoder 의 동작을 알아보기 위한 테스트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.14
 */
class BCryptPasswordEncoderTests {
    @Test
    @DisplayName("같은 평문에 대해서는 어떤 BCryptPasswordEncoder 객체를 쓰던 검증이 가능하다..")
    void test1() {
        BCryptPasswordEncoder aEncoder = new BCryptPasswordEncoder();
        BCryptPasswordEncoder bEncoder = new BCryptPasswordEncoder();

        assertTrue(bEncoder.matches("1234", aEncoder.encode("1234")));
    }

    @Test
    @DisplayName("같은 평문이라도 다른 BCryptPasswordEncoder 객체에선 서로 다른 해시값을 가진다.")
    void test2() {
        BCryptPasswordEncoder aEncoder = new BCryptPasswordEncoder();
        BCryptPasswordEncoder bEncoder = new BCryptPasswordEncoder();

        String password = "1234";
        assertThat(aEncoder.encode(password), is(not(equalTo(bEncoder.encode(password)))));
    }

    @Test
    @DisplayName("평문을 알아야 BCryptoEncoder 로 비밀번호가 일치하는 지 알 수 있다.")
    void test3() {
        BCryptPasswordEncoder aEncoder = new BCryptPasswordEncoder();

        String password = "1234";
        String encodedPassword = "$2a$10$30tERj8td6hFOOVnsYjJn.YF7rcdh53Opow6B//OUgviRfdVqFi0W";

        assertTrue(aEncoder.matches(password, encodedPassword));
        assertFalse(aEncoder.matches(encodedPassword, encodedPassword));
    }
}
