package nextstep.authentication.unit;

import nextstep.authentication.application.GithubClient;
import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.utils.UserInformation.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GithubClient 관련 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Transactional
public class GithubClientTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private GithubClient githubClient;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute(this);
    }

    @DisplayName("토큰 요청 함수는, code를 입력 받으면 토큰을 반환한다.")
    @Test
    void requestGithubTokenTest() {
        // when
        String accessToken = githubClient.requestGithubToken(사용자1.getCode());

        // then
        assertThat(accessToken).isEqualTo(사용자1.getAccessToken());
    }

    @DisplayName("프로필 요청 함수는, 깃허브에서 발급받은 토큰을 입력하면 사용자 프로필을 반환한다.")
    @Test
    void requestGithubProfileTest() {
        // given
        String accessToken = githubClient.requestGithubToken(사용자1.getCode());

        // when
        GithubProfileResponse actual = githubClient.requestGithubProfile(accessToken);

        // then
        assertThat(actual).isEqualTo(new GithubProfileResponse(사용자1.getEmail(), 사용자1.getAge()));
    }
}
