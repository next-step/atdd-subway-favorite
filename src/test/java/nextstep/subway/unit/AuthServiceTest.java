package nextstep.subway.unit;

import nextstep.error.exception.BusinessException;
import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AuthServiceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    AuthService authService;

    @Test
    @DisplayName("이메일, 패스워드가 일치하는 멤버가 존재한다면 토큰반환")
    void createToken() {
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }
    @Test
    @DisplayName("패스워드가 일치하지 않는다면 예외 발생")
    void passwordNotEqual() {
        TokenRequest tokenRequest = new TokenRequest(EMAIL, "nopassword");
        assertThatThrownBy(() -> authService.createToken(tokenRequest))
                .isExactlyInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("일치하는 이메일의 회원이 존재하지 않는다면 예외 발생")
    void nonExistEmail() {
        TokenRequest tokenRequest = new TokenRequest("nonExist@email.com", "password");
        assertThatThrownBy(() -> authService.createToken(tokenRequest))
                .isExactlyInstanceOf(BusinessException.class);
    }


}
