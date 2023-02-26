package nextstep.subway.unit;

import nextstep.DataLoader;
import nextstep.member.application.GithubClient;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.LoginService;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.stub.GithubResponses;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Import({JwtTokenProvider.class, DataLoader.class, GithubClient.class})
@DataJpaTest
public class LoginServiceTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DataLoader dataLoader;
    @Autowired
    GithubClient githubClient;
    LoginService loginService;


    @BeforeEach
    void setUp() {
        loginService = new LoginService(memberRepository, jwtTokenProvider, githubClient);
        dataLoader.loadData();
    }

    @Test
    @DisplayName("토큰 생성 테스트 : 존재하지 않는 이메일")
    void test1() {
        assertThatThrownBy(() -> {
            loginService.createToken(new TokenRequest("invalid@email.com", "password"));
        }).hasMessageContaining("존재하지 않는 Email 입니다");
    }

    @Test
    @DisplayName("토큰 생성 테스트 : 잘못된 비밀번호")
    void test2() {
        assertThatThrownBy(() -> {
            loginService.createToken(new TokenRequest("admin@email.com", "invalid"));
        }).hasMessageContaining("잘못된 비밀번호 입니다");
    }

    @Test
    @DisplayName("토큰 생성 테스트 : 정상")
    void test3() {
        String token = loginService.createToken(new TokenRequest("admin@email.com", "password")).getAccessToken();
        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo("admin@email.com");
    }

    @Test
    @DisplayName("Github Token 테스트 : 정상")
    void test4() {
        GithubResponses 사용자1 = GithubResponses.사용자1;
        GithubClient githubClient1 = mock(GithubClient.class);
        when(githubClient1.getAccessTokenFromGithub(사용자1.getCode())).thenReturn(사용자1.getAccessToken());
        loginService = new LoginService(memberRepository, jwtTokenProvider, githubClient1);

        GithubAccessTokenResponse githubToken = loginService.getGithubToken(사용자1.getCode());
        assertThat(githubToken.getAccessToken()).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    @DisplayName("Authorization Fake 서버 테스트 : 정상")
    void test5() {
        GithubResponses 사용자1 = GithubResponses.사용자1;
        GithubAccessTokenResponse response = loginService.getAuth(new GithubAccessTokenRequest(사용자1.getCode(), 사용자1.getEmail(), 사용자1.getAccessToken()));
        assertThat(response.getAccessToken()).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    @DisplayName("Authorization Fake 서버 테스트 : 실패")
    void test6() {
        assertThatThrownBy(() -> {
            loginService.getAuth(new GithubAccessTokenRequest("", null, null));
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
