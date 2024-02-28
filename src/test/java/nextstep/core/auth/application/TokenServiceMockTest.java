package nextstep.core.auth.application;

import nextstep.core.auth.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.core.member.fixture.MemberFixture.브라운;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceMockTest {

    TokenService tokenService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    GithubClient githubClient;

    @BeforeEach
    void 사전_토큰_서비스_생성() {
        tokenService = new TokenService(jwtTokenProvider, githubClient);
    }

    @Nested
    class 깃허브_프로필_요청 {
        @Nested
        class 성공 {

            final String code = "Temp Code";

            /**
             * When  코드를 통해 깃허브 프로필을 요청할 경우
             * Then  정상적으로 프로필을 전달받는다.
             */
            @Test
            void 코드로_깃허브_프로필_요청() {
                // given
                when(githubClient.requestGithubProfile(code)).thenReturn(new GithubProfileResponse(브라운.이메일));

                // when
                GithubProfileResponse githubProfileResponse = tokenService.requestGithubProfile(code);

                // then
                assertThat(githubProfileResponse.getEmail()).isEqualTo(브라운.이메일);
            }
        }
    }
}
