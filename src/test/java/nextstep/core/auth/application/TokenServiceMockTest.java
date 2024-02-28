package nextstep.core.auth.application;

import nextstep.core.auth.application.dto.TokenResponse;
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
    JwtTokenProvider jwtTokenProvider;

    @Mock
    GithubClient githubClient;

    @BeforeEach
    void 사전_토큰_서비스_생성() {
        tokenService = new TokenService(jwtTokenProvider, githubClient);
    }

    @Nested
    class 깃허브부터_토큰_발급 {
        @Nested
        class 성공 {

            final String code = "Temp Code";
            final String token = "Bearer TOKEN ";

            final String email = "test@test.com";
            final String password = "PASSWORD";
            final int age = 20;

            /**
             * When  코드를 통해 깃허브에 회원정보를 요청한다.
             * When     응답받은 회원정보에 일치하는 회원을 조회한다.
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 깃허브로_회원가입된_회원의_토큰_발급_요청() {
                // given
                when(jwtTokenProvider.createToken(email)).thenReturn(token);

                // when
                TokenResponse tokenResponse = tokenService.createTokenByGithub(email);

                // then
                assertThat(tokenResponse.getAccessToken()).isNotBlank();
            }
        }
    }
}
