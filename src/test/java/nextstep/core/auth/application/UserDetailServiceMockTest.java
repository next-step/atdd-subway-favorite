package nextstep.core.auth.application;

import nextstep.core.auth.application.dto.GithubProfileResponse;
import nextstep.core.auth.application.dto.TokenResponse;
import nextstep.core.member.application.MemberService;
import nextstep.core.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.core.auth.fixture.GithubMemberFixture.김영호;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceMockTest {

    UserDetailService userDetailService;

    @Mock
    TokenService tokenService;

    @Mock
    MemberService memberService;

    @BeforeEach
    void 사전_유저_서비스_생성() {
        userDetailService = new UserDetailService(tokenService, memberService);
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
                when(tokenService.requestGithubProfile(김영호.코드)).thenReturn(new GithubProfileResponse(김영호.이메일));
                when(memberService.findOrCreate(김영호.이메일)).thenReturn(new Member(김영호.이메일, "password1!"));
                when(tokenService.createToken(김영호.이메일)).thenReturn(new TokenResponse(김영호.토큰));

                // when
                TokenResponse tokenResponse = userDetailService.createTokenByGithub(김영호.코드);

                // then
                assertThat(tokenResponse.getAccessToken()).isEqualTo(김영호.토큰);

                verify(tokenService, times(1)).requestGithubProfile(any(String.class));
                verify(memberService, times(1)).findOrCreate(any(String.class));
                verify(tokenService, times(1)).createToken(any(String.class));
            }
        }
    }
}
