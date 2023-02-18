package nextstep.subway.unit;

import static nextstep.subway.utils.GitHubResponses.*;

import org.assertj.core.api.Assertions;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.member.application.GitHubClient;
import nextstep.subway.utils.GitHubResponses;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GitHubClientTest {

    @Autowired
    private GitHubClient gitHubClient;

    @DisplayName("권한증서로 GitHub Access Token을 발급한다.")
    @EnumSource(value = GitHubResponses.class)
    @ParameterizedTest
    void getAccessTokenFromGithub(GitHubResponses responses) {
        // when
        String accessTokenFromGithub = gitHubClient.getAccessTokenFromGithub(responses.getCode());

        // then
        Assertions.assertThat(accessTokenFromGithub).isEqualTo(responses.getAccessToken());
    }
}
