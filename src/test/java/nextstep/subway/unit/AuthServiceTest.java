package nextstep.subway.unit;

import nextstep.exception.member.PasswordNotEqualException;
import nextstep.member.application.AuthService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.MemberResponse;
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
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        TokenResponse tokenResponse = authService.loginMember(tokenRequest);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("패스워드가 일치하지 않는다면 예외 발생")
    void passwordNotEqual() {
        TokenRequest tokenRequest = new TokenRequest(EMAIL, "nopassword");
        assertThatThrownBy(() -> authService.loginMember(tokenRequest))
                .isExactlyInstanceOf(PasswordNotEqualException.class);
    }

    @Test
    @DisplayName("일치하는 이메일의 회원이 존재하지 않는다면 예외 발생")
    void nonExistEmail() {
        TokenRequest tokenRequest = new TokenRequest("nonExist@email.com", "password");
        assertThatThrownBy(() -> authService.loginMember(tokenRequest))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("토큰으로 부터 멤버 정보 가져오기")
    void getInfoFromToken() {
        // given
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        TokenResponse tokenResponse = authService.loginMember(tokenRequest);

        // when
        MemberResponse memberInfo = authService.findMemberOfMine(tokenResponse.getAccessToken());

        // then
        assertThat(memberInfo.getEmail()).isEqualTo(EMAIL);
    }

    /*@Test
    @DisplayName("토큰 만료 테스트")
    void tokenIsExpired() {
        // given
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        TokenResponse tokenResponse = authService.loginMember(tokenRequest);

        // then
        assertThatThrownBy(() -> authService.findMemberOfMine(tokenResponse.getAccessToken()))
                .isExactlyInstanceOf(AuthTokenIsExpiredException.class);
    }*/
}
