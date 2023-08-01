package nextstep.auth.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    @Test
    void createAndValidateToken() throws InterruptedException {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

        String token = jwtTokenProvider.createToken("principal", "member");

        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo("principal");
        assertThat(jwtTokenProvider.getRoles(token)).isEqualTo("member");
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();

        Thread.sleep(1000);
        assertThat(jwtTokenProvider.validateToken(token)).isFalse();
    }

}