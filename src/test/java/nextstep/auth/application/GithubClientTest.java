package nextstep.auth.application;

import nextstep.auth.token.oauth2.github.GithubClient;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.utils.GithubTestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    
    @Autowired
    private GithubClient githubClient;

    @DisplayName("엑세스 토큰 요청시 코드가 정상이라면 엑세스 토큰을 리턴한다.")
    @Test
    void getAccessTokenFromGithub() {
        // when
        String accessToken = githubClient.getAccessTokenFromGithub(GithubTestUser.USER1.getCode());

        // then
        assertThat(accessToken).isNotBlank();
    }

    @DisplayName("엑세스 토큰 요청시 코드가 비정상이라면 에러를 던진다.")
    @Test
    void getAccessTokenFromGithub_not_available_code() {
        // when then
        assertThatThrownBy(() -> githubClient.getAccessTokenFromGithub("code"))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @DisplayName("엑세스 토큰으로 깃허브 프로필 요청 시 해당 회원에 프로필 정보를 리턴한다.")
    @Test
    void getGithubProfileFromGithub() {
        // given
        String accessToken = githubClient.getAccessTokenFromGithub(GithubTestUser.USER1.getCode());

        // when
        GithubProfileResponse githubProfileFromGithub = githubClient.getGithubProfileFromGithub(accessToken);

        // then
        assertThat(githubProfileFromGithub.getEmail()).isEqualTo(GithubTestUser.USER1.getEmail());
        assertThat(githubProfileFromGithub.getAge()).isEqualTo(GithubTestUser.USER1.getAge());
    }

    @DisplayName("엑세스 토큰으로 깃허브 프로필 요청 시 정상적인 엑시스 토큰이 아닐경우 애러를 던진다.")
    @Test
    void getGithubProfileFromGithub_not_available_accessToken() {
        // when then
        assertThatThrownBy(() -> githubClient.getGithubProfileFromGithub("testToken"))
                .isExactlyInstanceOf(RuntimeException.class);
    }

}
