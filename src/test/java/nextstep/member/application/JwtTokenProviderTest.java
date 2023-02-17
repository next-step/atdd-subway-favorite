package nextstep.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Jwt 관련 기능 테스트")
@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("admin@email.com", "1234", 26, List.of(RoleType.ROLE_ADMIN.name()));
    }

    @DisplayName("멤버를 기준으로 토큰을 생성한다.")
    @Test
    public void createTokenFromMember() {
        String token = jwtTokenProvider.createToken(member);

        assertAll(
                () -> assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo(member.getEmail()),
                () -> assertThat(jwtTokenProvider.getRoles(token)).containsExactly(RoleType.ROLE_ADMIN.name())
        );
    }

    @DisplayName("멤버와 시간을 기준으로 토큰을 생성한다.")
    @Test
    public void createTokenFromMemberAndDate() {
        String token = jwtTokenProvider.createToken(member, LocalDateTime.now());

        assertAll(
                () -> assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo(member.getEmail()),
                () -> assertThat(jwtTokenProvider.getRoles(token)).containsExactly(RoleType.ROLE_ADMIN.name())
        );
    }
}
