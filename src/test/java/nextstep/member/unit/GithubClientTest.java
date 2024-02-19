package nextstep.member.unit;

import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.Spring;
import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    // 1. github token 받아오기
    // 2. github 사용자 정보 받아오기

    @Test
    void requestGithubToken() {
        // given
        GithubClient githubClient = new GithubClient();

        // when
        String githubToken = githubClient.requestGithubToken(GithubResponses.사용자1.getCode());

        // then
        assertThat(githubToken).isNotBlank();
        assertThat(githubToken).isEqualTo(GithubResponses.사용자1.getAccessToken());
    }

    @Test
    void requestGithubProfile() {
        // given
        GithubClient githubClient = new GithubClient();

        // when
        GithubProfileResponse githubProfile = githubClient.requestGithubProfile(GithubResponses.사용자1.getAccessToken());

        // then
        assertThat(githubProfile.getEmail()).isEqualTo(GithubResponses.사용자1.getEmail());
    }
}
