package nextstep.core.auth.application;

import nextstep.core.auth.application.dto.GithubProfileResponse;
import nextstep.core.auth.application.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.core.auth.fixture.GithubMemberFixture.김영호;
import static nextstep.core.auth.fixture.GithubMemberFixture.황병국;
import static nextstep.core.member.fixture.MemberFixture.브라운;
import static nextstep.core.member.fixture.MemberFixture.존슨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceMockTest {

    TokenService tokenService;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    GithubClient githubClient;

    @BeforeEach
    void 사전_토큰_서비스_생성() {
        tokenService = new TokenService(userDetailsService, jwtTokenProvider, githubClient);
    }

    @Nested
    class 깃허브를_통해_토큰_요청 {

        @Nested
        class 성공 {

            /**
             * When  코드를 통해 깃허브에 회원정보를 요청한다.
             * When     응답받은 회원정보에 일치하는 회원을 조회한다.
             * When         일치하는 회원이 있을 경우
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 토큰발급() {
                // given
                when(githubClient.requestGithubProfile(김영호.코드)).thenReturn(new GithubProfileResponse(김영호.이메일));
                when(userDetailsService.findOrCreate(김영호.이메일)).thenReturn(김영호.이메일);
                when(jwtTokenProvider.createToken(김영호.이메일)).thenReturn(김영호.토큰);

                // when
                TokenResponse tokenResponse = tokenService.createTokenByGithub(김영호.코드);

                // then
                assertThat(tokenResponse.getAccessToken()).isEqualTo(김영호.토큰);

                verify(githubClient, times(1)).requestGithubProfile(any(String.class));
                verify(userDetailsService, times(1)).findOrCreate(any(String.class));
                verify(jwtTokenProvider, times(1)).createToken(any(String.class));
            }
        }
    }

    @Nested
    class 깃허브_프로필_요청 {
        @Nested
        class 성공 {

            /**
             * When  코드를 통해 깃허브 프로필을 요청할 경우
             * Then  정상적으로 프로필을 전달받는다.
             */
            @Test
            void 코드로_깃허브_프로필_요청() {
                // given
                when(githubClient.requestGithubProfile(황병국.코드)).thenReturn(new GithubProfileResponse(황병국.이메일));

                // when
                GithubProfileResponse githubProfileResponse = tokenService.requestGithubProfile(황병국.코드);

                // then
                assertThat(githubProfileResponse.getEmail()).isEqualTo(황병국.이메일);
            }
        }
    }
}
