package nextstep.auth.acceptance.unit.github;

import nextstep.auth.oauth2.github.client.GithubClient;
import nextstep.auth.oauth2.github.dto.GithubProfileResponse;
import nextstep.global.error.exception.BusinessException;
import nextstep.support.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static nextstep.auth.acceptance.utils.GithubMockUser.다니엘;
import static nextstep.auth.acceptance.utils.GithubMockUser.팜하니;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@AcceptanceTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GitHubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    @DisplayName("올바른 코드를 전달 받았을 때 엑세스 토큰을 발급받을 수 있다.")
    public void successGetAccessToken() {
        // when
        String 엑세스_토큰 = githubClient.getAccessTokenFromGithub(팜하니.getCode());

        // then
        assertThat(엑세스_토큰).isEqualTo(팜하니.getAccessToken());
    }

    @Test
    @DisplayName("올바르지 않은 코드를 전달 받았을 때 엑세스 토큰을 발급 받을 수 없다.")
    public void failedGetAccessTokenWithWrongCode() {
        // when & then
        assertThatThrownBy(() -> githubClient.getAccessTokenFromGithub("wrong_code"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("올바른 엑세스 토큰를 전달 받았을 때 회원 정보를 조회할 수 있다.")
    public void successGetGithubProfile() {
        // when
        GithubProfileResponse 다니엘_정보 = githubClient.getGithubProfileFromGithub(다니엘.getAccessToken());

        // then
        assertAll(
                () -> assertThat(다니엘_정보.getUsername()).isEqualTo(다니엘.getEmail()),
                () -> assertThat(다니엘_정보.getAge()).isEqualTo(다니엘.getAge())
        );
    }

    @Test
    @DisplayName("올바르지 않은 엑세스 토큰를 전달 받았을 때 회원 정보를 조회할 수 없다.")
    public void failedGetGithubProfileWithWrongAccessToken() {
        // when & then
        assertThatThrownBy(() -> githubClient.getGithubProfileFromGithub("wrong_access_token"))
                .isInstanceOf(BusinessException.class);
    }

}
