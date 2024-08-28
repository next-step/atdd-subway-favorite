package nextstep.member.unit;

import nextstep.common.exception.MemberNotFoundException;
import nextstep.common.exception.PathNotFoundException;
import nextstep.member.GithubResponses;
import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    @Autowired
    private GithubClient githubClient;

    @DisplayName("사용자의 코드로 깃헙 토큰을 요청했을 때 사용자 토큰이 리턴된다.")
    @Test
    void requestGithubAecessToken(){
        String code = GithubResponses.사용자1.getCode();
        String githubAccessToken = githubClient.requestGithubAeccessToken(code);

        assertThat(githubAccessToken).isNotBlank();
        assertThat(githubAccessToken).isEqualTo(GithubResponses.사용자1.getAccessToken());
    }

    @DisplayName("알수 없는 코드로 깃헙 토큰을 요청했을 때 예외가 발생한다.")
    @Test
    void requestNotGithubAecessToken(){
        String code = "알수없는 코드";

        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> githubClient.requestGithubAeccessToken(code));

    }

    @DisplayName("사용자의 코드로 깃헙 정보를 요청했을 때 사용자 정보가 리턴된다.")
    @Test
    void requestGithubUser(){
        String code = GithubResponses.사용자2.getCode();
        String githubAccessToken = githubClient.requestGithubAeccessToken(code);

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubUser(githubAccessToken);

        assertThat(githubProfileResponse.getEmail()).isEqualTo(GithubResponses.사용자2.getEmail());
        assertThat(githubProfileResponse.getAge()).isEqualTo(GithubResponses.사용자2.getAge());

    }

    @DisplayName("알수없는 토큰으로 깃헙 정보를 요청했을 때 예외가 발생한다.")
    @Test
    void requestNotGithubUser(){
        String githubAccessToken = "알수없는 토큰";

        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> githubClient.requestGithubUser(githubAccessToken));

    }
}
