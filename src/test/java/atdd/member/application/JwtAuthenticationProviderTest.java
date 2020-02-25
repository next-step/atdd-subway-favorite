package atdd.member.application;

import static org.assertj.core.api.Assertions.assertThat;

import atdd.member.security.JwtAuthenticationProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JwtAuthenticationProvider.class)
class JwtAuthenticationProviderTest {

    private static final String TEST_EMAIL = "seok2@gmail.com";
    @Autowired
    private JwtAuthenticationProvider provider;

    @Test
    @DisplayName("토큰 생성")
    void create() {
        assertThat(provider.create(TEST_EMAIL)).isNotNull();
    }

    @Test
    @DisplayName("토큰에서 E-MAIL 추출")
    void getEmailFromToken() {
        String token = provider.create(TEST_EMAIL);
        assertThat(provider.getEmailFromToken(token)).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("토큰 기간 만료 체크")
    void isExpired() {
        String token = provider.create(TEST_EMAIL);
        assertThat(provider.isExpired(token)).isFalse();
    }
}