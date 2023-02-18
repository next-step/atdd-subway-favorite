package nextstep.subway.unit;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    void createToken() {
        TokenRequest tokenRequest = new TokenRequest("test@email.com", "password");
        TokenResponse tokenResponse = authService.loginMember(tokenRequest);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }
}