package nextstep.subway.unit;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.GithubLoginRequest;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.subway.GithubClientTestContextConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(GithubClientTestContextConfiguration.class)
@Transactional
public class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member(GithubResponses.사용자1.getEmail(), "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
    }

    @DisplayName("일반 로그인")
    @Test
    void login() {
        // given
        final TokenRequest tokenRequest = new TokenRequest(member.getEmail(), member.getPassword());

        // when
        final TokenResponse tokenResponse = authService.login(tokenRequest);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    @DisplayName("Github 로그인")
    @Test
    void githubOAuth() {
        // given
        final GithubLoginRequest githubLoginRequest = new GithubLoginRequest(GithubResponses.사용자1.getCode());

        // when
        final TokenResponse tokenResponse = authService.githubLogin(githubLoginRequest);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }
}
