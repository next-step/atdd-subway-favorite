package nextstep.auth.acceptance;

import static nextstep.auth.fixture.GithubResponses.사용자1;
import static nextstep.auth.fixture.GithubResponses.사용자2;
import nextstep.auth.oauth.github.GithubClient;
import nextstep.auth.oauth.github.GithubProfileResponse;
import nextstep.utils.context.AcceptanceTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@AcceptanceTest
class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    @DisplayName("깃허브 토큰을 요청한다.")
    void 깃허브_토큰_요청() {
        //given
        String code = 사용자1.getCode();

        //when
        String 깃허브_토큰_응답 = githubClient.requestGithubToken(code);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(깃허브_토큰_응답).isEqualTo(사용자1.getAccessToken());
        });

    }

    @Test
    @DisplayName("사용자 정보를 요청한다.")
    void 사용자_정보_요청() {
        //given
        String accessToken = 사용자2.getAccessToken();

        //when
        GithubProfileResponse 깃허브_사용자_정보_응답 = githubClient.requestGithubProfile(accessToken);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(깃허브_사용자_정보_응답.getEmail()).isEqualTo(사용자2.getEmail());
        });

    }
}
