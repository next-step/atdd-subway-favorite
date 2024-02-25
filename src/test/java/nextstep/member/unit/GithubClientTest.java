package nextstep.member.unit;

import nextstep.member.application.GithubClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @DisplayName("깃허브 토큰 요청")
    @Test
    void requestToken() {
        String code = "code";

        GithubClient client = new GithubClient();

        String accessToken = client.requestToken(code);

        Assertions.assertThat(accessToken).isEqualTo("accessToken");
    }
}
