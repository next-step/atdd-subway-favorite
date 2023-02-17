package nextstep.member.application;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Jwt 관련 기능 테스트")
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
    }

    @DisplayName("멤버를 기준으로 principal을 생성한다.")
    @Test
    void createPrincipal() {
        String principal = jwtTokenProvider.createPrincipalFrom(new Member("admin@email.com", "1234", 26));

        assertThat(principal).isEqualTo("admin@email.com");
    }
}
