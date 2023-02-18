package nextstep.subway.unit;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("이메일, 패스워드가 일치하는 멤버가 존재한다면 토큰반환")
    void createToken() {
        TokenRequest tokenRequest = new TokenRequest("admin@email.com", "password");
        TokenResponse tokenResponse = authService.loginMember(tokenRequest);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }
}