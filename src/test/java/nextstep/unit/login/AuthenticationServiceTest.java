package nextstep.unit.login;

import nextstep.login.application.AuthenticationService;
import nextstep.login.application.dto.LoginResponse;
import nextstep.utils.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.fixture.MemberFixture.회원_ALEX;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 인증 기능")
@SpringBootTest
@Transactional
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        dataLoader.loadData();
    }

    @Nested
    @DisplayName("로그인 진행 시")
    class Context_with_login {

        @Test
        @DisplayName("Jwt 토큰으로 생성된 AccessToken을 반환한다")
        void it_returns_access_token_made_with_jwt_token() throws Exception {
            LoginResponse login = authenticationService.login(회원_ALEX.이메일(), 회원_ALEX.비밀번호());

            JWT_토큰_형식으로_반환된다(login);
        }
    }


    private void JWT_토큰_형식으로_반환된다(LoginResponse login) {
        assertThat(login.getAccessToken().split("\\.")).hasSize(3);
    }
}
