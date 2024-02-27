package nextstep.member.application;

import nextstep.auth.application.GithubClient;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.utils.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    // 1. github token using code
    // 2. github profile using github token
    @Autowired
    private GithubClient githubClient;
    @Test
    void requestGithubToken() {
        String githubToken = githubClient.requestGithubToken("code");
        assertThat(githubToken).isNotBlank();
    }

    @Test
    void requestUserProfile() {
        String access_token = "access_token";
        GithubProfileResponse response = githubClient.requestUserProfile(access_token);

        assertThat(response.getEmail()).isNotBlank();
    }

    @Test
    void requestGithubToken_with_user(){
        String githubToken = githubClient.requestGithubToken(사용자1.getCode());
        assertThat(githubToken).isEqualTo(사용자1.getGithubToken());
    }

    @Test
    void requestUserProfile_with_user() {
        GithubProfileResponse response = githubClient.requestUserProfile(githubClient.requestGithubToken(사용자1.getCode()));
        assertThat(response.getEmail()).isEqualTo(사용자1.getEmail());
    }
}
