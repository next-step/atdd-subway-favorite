package nextstep.subway.unit;

import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenProviderTest {

    @Test
    void token() {
        JwtTokenProvider jwtTokenProvider = getTarget();
        String email = "email@email.com";

        String token = jwtTokenProvider.createToken(email, List.of());
        List<String> roles = jwtTokenProvider.getRoles(token);

        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo(email);
//        TODO: role assert
    }

    private JwtTokenProvider getTarget() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "atdd-secret-key");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);

        return jwtTokenProvider;
    }
}
