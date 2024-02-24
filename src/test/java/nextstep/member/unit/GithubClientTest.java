package nextstep.member.unit;

import nextstep.member.application.GithubClient;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    // 1. github token using code
    // 2. github profile using github token

    private static final String TOKEN_ISSUE_FAIL_MESSAGE = "ACCESS TOKEN ISSUE FAILED";

    @Autowired
    private GithubClient githubClient;

    @DisplayName("등록된 코드로 깃허브 토큰을 요청하면 토큰을 발급 받는다.")
    @Test
    void 깃허브_토큰_요청_성공() {
        // given
        String code = GithubResponses.사용자_홍길동.getCode();

        // when
        String githubToken = githubClient.requestGithubToken(code);

        // then
        assertThat(githubToken).isNotBlank();
        assertThat(githubToken).isEqualTo(GithubResponses.사용자_홍길동.getAccessToken());
    }


    @DisplayName("등록되지 않은 코드로 깃허브 토큰을 요청하면 토큰을 발급받지 못한다.")
    @Test
    void 깃허브_토큰_요청_실패() {
        // given
        String code = "qwer1234";

        // when
        String githubToken = githubClient.requestGithubToken(code);

        // then
        assertThat(githubToken).isEqualTo(TOKEN_ISSUE_FAIL_MESSAGE);
    }

//    @DisplayName("발급받은 깃허브 토큰으로 리소스를 요청하면 이메일 정보를 응답받는다.")
//    @Test
//    void 깃허브_리소스_요청_성공() {
//        // given
//        String code = GithubResponses.사용자_홍길동.getCode();
//
//        // when
//        String githubToken = githubClient.requestGithubResource(code);
//
//        // then
//        assertThat(githubToken).isNotBlank();
//        assertThat(githubToken).isEqualTo(GithubResponses.사용자_홍길동.getAccessToken());
//    }




//    @Test
//    void requestGithubProfile() {
//        GithubClient githubClient = new GithubClient();
//
//        // GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile("access_token");
//
//        // assertThat
//    }


}
