package nextstep.subway.unit;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.member.application.TokenService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.subway.utils.GitHubResponses;

@SpringBootTest
class TokenServiceTest {

    private static final String EMAIL = "testuser@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member(EMAIL, PASSWORD, 20, List.of(RoleType.ROLE_MEMBER.name())));
    }

    @DisplayName("사용자의 이메일과 비밀번호로 JWT 토큰을 생성한다.")
    @Test
    void createToken() {
        // when
        TokenResponse response = tokenService.createToken(EMAIL, PASSWORD);

        // then
        assertThat(response.getAccessToken()).isNotBlank();
    }

    @DisplayName("권한증서(code)로 GitHub 토큰을 생성한다.")
    @Test
    void createGitHubToken() {
        // when
        TokenResponse response = tokenService.createGitHubToken(GitHubResponses.사용자1.getCode());

        // then
        assertThat(response.getAccessToken()).isNotBlank();
    }
}
