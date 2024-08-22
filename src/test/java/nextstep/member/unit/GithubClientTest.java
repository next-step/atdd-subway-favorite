package nextstep.member.unit;

import nextstep.auth.domain.GithubClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

// 테스트 컨트롤러의 포트가 고정되어 있으므로 DEFINED_PORT 사용
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("깃허브 클라이언트 테스트")
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    void 깃허브_토큰_요청() {
        //given
        GithubUser 사용자1 = GithubUser.사용자1;

        //when
        var 깃허브_토큰 = githubClient.requestGithubToken(사용자1.getCode()).getAccessToken();

        //then
        assertThat(깃허브_토큰).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    void 깃허브_사용자_정보_요청() {
        //given
        var 사용자1 = GithubUser.사용자1;
        var 깃허브_토큰 = githubClient.requestGithubToken(사용자1.getCode()).getAccessToken();

        //when
        var 깃허브_사용자 = githubClient.requestGithubProfile(깃허브_토큰);

        //then
        assertThat(깃허브_사용자.getEmail()).isEqualTo(사용자1.getEmail());
    }
}
