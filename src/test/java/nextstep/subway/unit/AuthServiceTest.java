package nextstep.subway.unit;

import nextstep.exception.member.MemberNotFoundException;
import nextstep.exception.member.PasswordNotEqualException;
import nextstep.member.application.AuthService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AuthServiceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("이메일, 패스워드가 일치하는 멤버가 존재한다면 토큰반환")
    void createToken() {
        //given
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);

        //when
        TokenResponse tokenResponse = authService.loginMember(tokenRequest);

        //then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("패스워드가 일치하지 않는다면 예외 던짐")
    void passwordNotEqual() {
        //given
        TokenRequest tokenRequest = new TokenRequest(EMAIL, "nopassword");

        //when,then
        assertThatThrownBy(() -> authService.loginMember(tokenRequest))
                .isExactlyInstanceOf(PasswordNotEqualException.class);
    }

    @Test
    @DisplayName("일치하는 이메일의 회원이 존재하지 않는다면 예외 던짐")
    void nonExistEmail() {
        //given
        TokenRequest tokenRequest = new TokenRequest("nonExist@email.com", "password");

        //when,then
        assertThatThrownBy(() -> authService.loginMember(tokenRequest))
                .isExactlyInstanceOf(MemberNotFoundException.class);
    }
}
