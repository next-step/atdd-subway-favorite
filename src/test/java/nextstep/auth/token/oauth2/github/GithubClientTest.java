package nextstep.auth.token.oauth2.github;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GithubClientTest extends AcceptanceTest {

    @Autowired
    private GithubClient githubClient;

    @DisplayName("Github Auth - 토큰 발급을 깃 허브 테스트 컨트롤러에서 받아온다.")
    @ParameterizedTest
    @CsvSource(value = {
            "aofijeowifjaoief, access_token_1",
            "fau3nfin93dmn, access_token_2",
            "afnm93fmdodf, access_token_3",
            "fm04fndkaladmd, access_token_4"
    })
    void getAccessToken(String code, String accessToken) {
        // given : 선행조건 기술

        // when : 기능 수행
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);

        // then : 결과 확인
        assertThat(accessTokenFromGithub).isEqualTo(accessToken);
    }

    @DisplayName("Github Auth - 토큰을 이용해 깃 허브 프로필을 받아온다.")
    @ParameterizedTest
    @CsvSource(value = {
            "aofijeowifjaoief, email1@email.com, 20",
            "fau3nfin93dmn, email2@email.com, 21",
            "afnm93fmdodf, email3@email.com, 22",
            "fm04fndkaladmd, email4@email.com, 23"
    })
    void getGithubProfileFromGithub(String code, String email, int age) {
        // given : 선행조건 기술
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);

        // when : 기능 수행
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        // then : 결과 확인
        assertThat(githubProfile.getEmail()).isEqualTo(email);
        assertThat(githubProfile.getAge()).isEqualTo(age);
    }
}