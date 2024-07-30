package nextstep.member.application;

import static nextstep.member.application.MemberService.*;
import static nextstep.member.application.dto.GithubResponses.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class DefaultTokenServiceTest {

    @Autowired
    private DefaultTokenService tokenService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("정상적으로 토큰이 생성된다")
    void createToken_Success() {
        // given
        String email = "test@example.com";
        String password = "password";
        memberService.createMember(new MemberRequest(email, password, 30));

        // when
        TokenResponse tokenResponse = tokenService.createToken(email, password);

        // then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 토큰을 생성하려고 하면 오류가 발생한다 발생하는 케이스")
    void createToken_MemberNotFound() {
        // given
        String email = "non-existent@example.com";
        String password = "password";

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tokenService.createToken(email, password))
            .withMessageContaining(MEMBER_NOT_FOUND_MESSAGE);
    }

    @Test
    @DisplayName("틀린 비밀번호로 토큰 생성을 요청하면 오류가 발생한다")
    void createToken_InvalidPassword() {
        // given
        String email = "test@example.com";
        String password = "password";
        memberService.createMember(new MemberRequest(email, password, 30));

        // when & then
        assertThatExceptionOfType(AuthenticationException.class)
            .isThrownBy(() -> tokenService.createToken(email, "wrong_password"));
    }

    @Test
    @DisplayName("정상적으로 Github를 통해 토큰이 생성된다.")
    void createTokenFromGithub_Success() {
        // given
        String code = 사용자1.getCode();

        // when
        TokenResponse tokenResponse = tokenService.createTokenFromGithub(code);

        // then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("잘못된 코드 값으로 Github를 통해 토큰을 생성하려고 할 때 오류가 발생한다")
    void createTokenFromGithub_InvalidAccessToken() {
        // given
        String code = "invalid_code";

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tokenService.createTokenFromGithub(code))
            .withMessageContaining("Invalid code");
    }

    @Test
    @DisplayName("github를 통해 토큰을 생성하려고 할 때 존재하지 않는 회원에 대해 회원이 가입되고 토큰이 생성된다")
    void createTokenFromGithub_NewMember() {
        // given
        String code = 사용자2.getCode();
        String email = 사용자2.getEmail();

        // when
        TokenResponse tokenResponse = tokenService.createTokenFromGithub(code);

        // then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();

        Member member = memberService.findMemberByEmailOrThrow(email);
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
    }
}