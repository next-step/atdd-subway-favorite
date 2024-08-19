package nextstep.member.unit;

import nextstep.member.application.GithubClient;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.utils.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GithubClient 관련 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@ActiveProfiles({"test", "databaseCleanup"})
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
    void githubAuth() {
        // when
        String accessToken = githubClient.requestGithubToken(사용자1.getCode());

        // then
        assertThat(accessToken).isEqualTo("access_token");
    }
}
