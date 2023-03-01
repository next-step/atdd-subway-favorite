package nextstep.subway.unit;

import nextstep.member.infra.GithubClient;
import nextstep.member.infra.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GithubClientTest {

    @Autowired
    private GithubClient testGithubClient;

    @DisplayName("깃헙 accessToken 받아오기")
    @Test
    void getAccessTokenFromGithub() {
        // when
        final String accessToken = testGithubClient.getAccessTokenFromGithub(GithubResponses.사용자1.getCode());

        // then
        assertThat(accessToken).isNotBlank();
    }

    @DisplayName("깃헙 프로필 받아오기")
    @Test
    void getGithubProfileFromGithub() {
        // given
        final String accessToken = testGithubClient.getAccessTokenFromGithub(GithubResponses.사용자1.getCode());

        // when
        final GithubProfileResponse githubProfileResponse = testGithubClient.getGithubProfileFromGithub(accessToken);

        // then
        assertThat(githubProfileResponse.getEmail()).isEqualTo(GithubResponses.사용자1.getEmail());
    }
}
