package nextstep.subway.unit;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";
    private static final Integer AGE = 10;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE, List.of(RoleType.ROLE_MEMBER.name())));
    }

    @DisplayName("이메일, 비밀번호로 jwt토큰을 생성한다.")
    @Test
    void createJwtToken() {
        final TokenResponse token = authService.createToken(new TokenRequest(EMAIL, PASSWORD));

        assertThat(token.getAccessToken()).isNotBlank();
    }
}
