package nextstep.subway.unit;

import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.RoleType;
import nextstep.subway.utils.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenProviderTest {

    @Test
    void token() {
        JwtTokenProvider jwtTokenProvider = SecurityUtil.getUnlimitedJwtTokenProvider();
        String email = "email@email.com";

        String token = jwtTokenProvider.createToken(email, List.of(RoleType.ROLE_MEMBER.toString()));

        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo(email);
        assertThat(jwtTokenProvider.getRoles(token)).contains(RoleType.ROLE_MEMBER.toString());
    }
}
