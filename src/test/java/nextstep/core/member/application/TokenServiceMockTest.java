package nextstep.core.member.application;

import nextstep.core.member.application.dto.GithubProfileRequest;
import nextstep.core.member.application.dto.TokenResponse;
import nextstep.core.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceMockTest {

    TokenService tokenService;

    @Mock
    MemberService memberService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    GithubClient githubClient;

    @BeforeEach
    void 사전_토큰_서비스_생성() {
        tokenService = new TokenService(memberService, jwtTokenProvider, githubClient);
    }

    @Nested
    class 깃허브부터_토큰_발급 {
        @Nested
        class 성공 {

            final String VALID_CODE = "Valid Code";
            final String VALID_TOKEN = "Valid Token";

            final String EMAIL = "test@test.com";
            final String PASSWORD = "PASSWORD";
            final int AGE = 20;

            /**
             * When  코드를 통해 깃허브에 회원정보를 요청한다.
             * When     응답받은 회원정보에 일치하는 회원을 조회한다.
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 깃허브로_회원가입된_회원의_토큰_발급_요청() {
                // given
                when(githubClient.requestMemberInfo(VALID_CODE)).thenReturn(new GithubProfileResponse(EMAIL));
                when(memberService.findOrCreate(EMAIL)).thenReturn(new Member(EMAIL, PASSWORD, AGE));
                when(jwtTokenProvider.createToken(EMAIL)).thenReturn(VALID_TOKEN);

                // when
                TokenResponse tokenResponse = tokenService.createTokenByGithub(VALID_CODE);

                // then
                assertThat(tokenResponse.getAccessToken()).isNotBlank();

                verify(githubClient, times(1)).requestMemberInfo(VALID_CODE);
                verify(memberService, times(1)).findOrCreate(any(String.class));
                verify(jwtTokenProvider, times(1)).createToken(EMAIL);
            }
        }
    }
}
