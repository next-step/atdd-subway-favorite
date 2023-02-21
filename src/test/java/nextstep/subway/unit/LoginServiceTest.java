package nextstep.subway.unit;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.LoginService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class LoginServiceTest {
    @Autowired
    LoginService loginService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰 생성 테스트 : 존재하지 않는 이메일")
    void test1() {
        assertThatThrownBy(() -> {
            loginService.createToken(new TokenRequest("invalid@email.com", "password"));
        }).hasMessageContaining("존재하지 않는 Email 입니다");
    }

    @Test
    @DisplayName("토큰 생성 테스트 : 잘못된 비밀번호")
    void test2() {
        assertThatThrownBy(() -> {
            loginService.createToken(new TokenRequest("admin@email.com", "invalid"));
        }).hasMessageContaining("잘못된 비밀번호 입니다");
    }

    @Test
    @DisplayName("토큰 생성 테스트 : 정상")
    void test3() {
        String token = loginService.createToken(new TokenRequest("admin@email.com", "password")).getAccessToken();
        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo("admin@email.com");
    }

}
