package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.dto.GithubProfileResponse;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DisplayName("깃허브 Oauth 관련 단위테스트")
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    @DisplayName("깃허브 토큰 발급")
    void 토큰_발급() {
        // given
        String code = GithubResponses.사용자1.getCode();

        // when
        String githubAccessToken = githubClient.requestGithubToken(code);

        // then
        assertThat(githubAccessToken).isNotBlank();
        assertThat(githubAccessToken).isEqualTo(GithubResponses.사용자1.getAccessToken());
    }

    @Test
    @DisplayName("깃허브 토큰으로 사용자 정보 조회")
    void 사용자_정보_조회() {
        // given
        String code = GithubResponses.사용자3.getCode();
        String githubAccessToken = githubClient.requestGithubToken(code);
        // when
        GithubProfileResponse githubUserInfo = githubClient.requestGithubUserInfo(githubAccessToken);

        // then
        assertThat(githubUserInfo).isNotNull();
        assertThat(githubUserInfo.getEmail()).isEqualTo(GithubResponses.사용자3.getEmail());
    }
}
