package atdd.member.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JwtAuthenticationProvider.class)
class JwtAuthenticationProviderTest {

    @Autowired
    private JwtAuthenticationProvider provider;

    @Test
    @DisplayName("토큰 생성")
    void create() {
        assertThat(provider.create("seok2@google.com")).isNotNull();
    }
}