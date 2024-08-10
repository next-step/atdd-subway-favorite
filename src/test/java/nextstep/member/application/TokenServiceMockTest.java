package nextstep.member.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.ProfileResponse;
import nextstep.utils.fakeMock.FakeClientRequester;
import nextstep.utils.fakeMock.FakeTokenProvider;
import nextstep.utils.fakeMock.FakeUserDetailsServiceImpl;
import nextstep.utils.dtoMock.GithubResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TokenServiceMockTest {

    @Mock
    private TokenService tokenService;
    private ProfileResponse 사용자1;
    private String 비밀번호 = "password";
    private String 틀린_비밀번호 = "!password";
    private String FAKE_토큰 = "createToken_success";
    private String 코드 ="code";

    @BeforeEach
    void setup() {
        tokenService = new TokenService(new FakeUserDetailsServiceImpl(), new FakeTokenProvider(), new FakeClientRequester());
        사용자1 = ProfileResponse.of(GithubResponse.사용자1.getEmail(), GithubResponse.사용자1.getAge());
    }

    @DisplayName("[createToken] 이메일과 비밀번호를 통해 Token을 생성한다.")
    @Test
    public void createToken_success() {
        // when
        var 생성된_토큰 = tokenService.createToken(사용자1.getEmail(), 비밀번호);

        // then
        assertThat(생성된_토큰.getAccessToken()).isEqualTo(FAKE_토큰);
    }

    @DisplayName("[createToken] 이메일과 틀린 비밀번호를 통해 Token 생성을 시도하면 예외가 발생한다.")
    @Test
    public void createToken_fail() {
        // when & then
        assertThrows(AuthenticationException.class, () -> tokenService.createToken(사용자1.getEmail(), 틀린_비밀번호));
    }

    @DisplayName("[getAuthToken] code를 통해 토큰을 발급받는다.")
    @Test
    public void getAuthToken_success() {
        // when
        var 생성된_토큰 = tokenService.getAuthToken(코드);

        // then
        assertThat(생성된_토큰.getAccessToken()).isEqualTo(FAKE_토큰);
    }

}

