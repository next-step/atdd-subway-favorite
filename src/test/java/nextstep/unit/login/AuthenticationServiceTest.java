package nextstep.unit.login;

import nextstep.login.application.AuthenticationService;
import nextstep.login.application.dto.response.LoginResponse;
import nextstep.member.application.exception.InvalidPasswordException;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.unit.support.ServiceTest;
import nextstep.utils.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.fixture.GitHubProfileFixture.ALEX_GITHUB;
import static nextstep.fixture.MemberFixture.비회원;
import static nextstep.fixture.MemberFixture.회원_ALEX;
import static nextstep.fixture.MemberFixture.회원_JADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("로그인 인증 기능")
class AuthenticationServiceTest extends ServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        dataLoader.loadData();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 이메일과_비밀번호로_로그인_진행_시 {

        @Nested
        @DisplayName("로그인이 성공하면")
        class Context_with_success_login {

            @Test
            @DisplayName("AccessToken을 반환한다")
            void it_returns_access_token() throws Exception {
                LoginResponse login = authenticationService.login(회원_ALEX.이메일(), 회원_ALEX.비밀번호());

                AccessToken이_반환된다(login);
            }
        }

        @Nested
        @DisplayName("찾을 수 없는 계정이면")
        class Context_with_member_not_found {

            @Test
            @DisplayName("MemberNotFoundException 예외를 던진다")
            void it_returns_exception() throws Exception {
                assertThatThrownBy(() -> authenticationService.login(비회원.이메일(), 비회원.비밀번호()))
                        .isInstanceOf(MemberNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않으면")
        class Context_with_invalid_password {

            @Test
            @DisplayName("InvalidPasswordException 예외를 던진다")
            void it_returns_exception() throws Exception {
                assertThatThrownBy(() -> authenticationService.login(회원_ALEX.이메일(), 회원_JADE.비밀번호()))
                        .isInstanceOf(InvalidPasswordException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 권한_증서_code로_로그인_진행_시 {

        @Nested
        @DisplayName("로그인이 성공하면")
        class Context_with_success_login {

            @Test
            @DisplayName("AccessToken을 반환한다")
            void it_returns_access_token() throws Exception {
                LoginResponse login = authenticationService.login(ALEX_GITHUB.권한_증서_코드());

                AccessToken이_반환된다(login);
            }
        }
    }

    private void AccessToken이_반환된다(LoginResponse login) {
        assertThat(login.getAccessToken()).isNotBlank();
    }
}
