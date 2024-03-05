package nextstep.auth.acceptance;

import nextstep.common.annotation.AcceptanceTest;
import nextstep.auth.application.GithubClient;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.auth.acceptance.GithubResponses.사용자1;
import static nextstep.auth.acceptance.GithubResponses.사용자2;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AcceptanceTest
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    @DisplayName("깃허브 토큰을 요청한다.")
    void 깃허브_토큰_요청() {
        //given
        String code = 사용자1.getCode();

        //when
        String accessToken = githubClient.requestGithubToken(code);

        //then
        assertThat(accessToken).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    @DisplayName("사용자 정보를 요청한다.")
    void 사용자_정보_요청() {
        //given
        String accessToken = 사용자2.getAccessToken();

        //when
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(accessToken);

        //then
        assertThat(githubProfileResponse.getEmail()).isEqualTo(사용자2.getEmail());
    }
}
