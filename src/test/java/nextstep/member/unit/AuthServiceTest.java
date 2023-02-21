package nextstep.member.unit;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        // given
        final Member member = new Member(EMAIL, PASSWORD, 20);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("토큰 생성 실패-비밀번호 미일치")
    void login_mismatchPwd() {
        // given
        final TokenRequest request = new TokenRequest(EMAIL, "other");

        // when
        // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("토큰 생성")
    void login() {
        // given
        final TokenRequest request = new TokenRequest(EMAIL, PASSWORD);

        // when
        TokenResponse token = authService.login(request);

        // then
        assertThat(token.getAccessToken()).isNotBlank();
    }
}
