package nextstep.member.unit;

import nextstep.member.GithubResponses;
import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    @Autowired
    private GithubClient githubClient;

    @DisplayName("깃헙 토큰 가져오기 테스트")
    @Test
    void requestGithubAecessToken(){
        String code = GithubResponses.사용자1.getCode();
        String githubAccessToken = githubClient.requestGithubAeccessToken(code);

        assertThat(githubAccessToken).isNotBlank();
        assertThat(githubAccessToken).isEqualTo(GithubResponses.사용자1.getAccessToken());
    }

    @DisplayName("깃헙 사용자 정보 가져오기 테스트")
    @Test
    void requestGithubUser(){
        String code = GithubResponses.사용자2.getCode();
        String githubAccessToken = githubClient.requestGithubAeccessToken(code);

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubUser(githubAccessToken);

        assertThat(githubProfileResponse.getEmail()).isEqualTo(GithubResponses.사용자2.getEmail());
        assertThat(githubProfileResponse.getAge()).isEqualTo(GithubResponses.사용자2.getAge());

    }
}
