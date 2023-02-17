package nextstep.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Jwt 관련 기능 테스트")
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        member = new Member("admin@email.com", "1234", 26, List.of(RoleType.ROLE_ADMIN.name()));
    }

    @DisplayName("멤버를 기준으로 토큰을 생성한다.")
    @Test
    public void createToken() {
        String token = jwtTokenProvider.createToken(member);

        assertAll(
                () -> assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo(member.getEmail()),
                () -> assertThat(jwtTokenProvider.getRoles(token)).containsExactly(RoleType.ROLE_ADMIN.name())
        );
    }
}
