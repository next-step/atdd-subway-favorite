package nextstep.core.auth.application;

import nextstep.common.annotation.ComponentTest;
import nextstep.core.auth.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.core.auth.fixture.GithubMemberFixture.모든_깃허브_회원;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("깃허브 클라이언트 컴포넌트 테스트")
@ComponentTest
public class GithubClientTest {

    @Autowired
    GithubClient githubClient;


    @Nested
    class 깃허브_회원정보_조회 {

        @Nested
        class 성공 {
            /**
             * When 코드를 통해 토큰을 요청하고 응답받았고
             * When    응답받은 토큰을 통해 깃허브 프로필 정보를 요청하고
             * When        프로필 정보를 응답받았을 경우
             * Then 응답받은 프로필 정보를 기반으로 회원 정보를 반환한다.
             */
            @Test
            void 회원_정보_반환() {
                모든_깃허브_회원().forEach(회원 -> {
                    // when
                    GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(회원.get코드());

                    // then
                    assertThat(githubProfileResponse.getEmail()).isEqualTo(회원.get이메일());
                });
            }
        }
    }
}
