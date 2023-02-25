package nextstep.member;

import nextstep.DataLoader;
import nextstep.member.fake.GithubResponse;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import static nextstep.member.LoginSteps.가짜_깃허브_권한증서_로그인;
import static nextstep.member.LoginSteps.가짜_베어러_인증_내_정보_조회;
import static nextstep.member.LoginSteps.베어러_인증_내_정보_조회;
import static nextstep.member.LoginSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    DataLoader dataLoader;

    @BeforeEach
    void init() {
        dataLoader.loadData();
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        var response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * given 사용자 1의 깃허브 권한증서를 통해
     * when  로그인을 시도하고 받은 토큰으로 내 정보를 조화하면
     * Then  사용자 1의 이메일을 얻을 수 있다.
     */
    @DisplayName("Github Auth")
    @Test
    void githubAuth() {

        var response = 가짜_깃허브_권한증서_로그인(GithubResponse.사용자1.getCode());
        String token = response.as(GithubResponse.class).getAccessToken();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(token).isNotBlank();

        var email = 가짜_베어러_인증_내_정보_조회(token).as(GithubResponse.class).getEmail();
        assertThat(email).isEqualTo(GithubResponse.사용자1.getEmail());
    }


}