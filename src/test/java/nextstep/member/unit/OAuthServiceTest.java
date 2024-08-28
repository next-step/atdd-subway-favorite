package nextstep.member.unit;

import nextstep.auth.application.GithubOAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

// 테스트 컨트롤러의 포트가 고정되어 있으므로 DEFINED_PORT 사용
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("OAuth 서비스 테스트")
public class OAuthServiceTest {

    @Autowired
    private GithubOAuthService githubOAuthService;

    @Test
    void OAuth_깃허브_토큰_요청() {
        //given
        GithubUser 사용자1 = GithubUser.사용자1;

        //when
        var 깃허브_토큰 = githubOAuthService.requestAccessToken(사용자1.getCode());

        //then
        assertThat(깃허브_토큰).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    void OAuth_깃허브_사용자_정보_요청() {
        //given
        var 사용자1 = GithubUser.사용자1;
        var 깃허브_토큰 = githubOAuthService.requestAccessToken(사용자1.getCode());

        //when
        var 깃허브_사용자 = githubOAuthService.requestUserProfile(깃허브_토큰);

        //then
        assertThat(깃허브_사용자.getEmail()).isEqualTo(사용자1.getEmail());
    }
}
