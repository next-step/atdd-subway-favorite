package nextstep.auth.application;

import nextstep.fake.GithubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GithubClient 테스트")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubClientTest {

    @Autowired
    private GithubClient githubClient;


    @DisplayName("GithubClient 요청 테스트")
    @Test
    void createInstance() {
        String accessToken = githubClient.requestGithubToken(GithubResponse.사용자1.getCode());
        assertThat(accessToken).isEqualTo(GithubResponse.사용자1.getAccessToken());
    }

}
