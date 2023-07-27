package nextstep.auth.token;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    @Test
    void createToken() {

        // given
        String name = "name";
        String role = "role";

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "testKey");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);

        // when & then
        String token = jwtTokenProvider.createToken(name, role);
        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo(name);
        assertThat(jwtTokenProvider.getRoles(token)).isEqualTo(role);
    }
}