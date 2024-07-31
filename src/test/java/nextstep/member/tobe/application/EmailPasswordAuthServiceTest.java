package nextstep.member.tobe.application;

import static nextstep.member.application.MemberService.*;
import static nextstep.member.application.dto.GithubResponses.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.tobe.AuthenticationException;
import nextstep.member.tobe.application.dto.TokenRequest;
import nextstep.member.tobe.application.dto.TokenResponse;
import nextstep.member.application.dto.GithubResponses;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmailPasswordAuthServiceTest {

    @Autowired
    private EmailPasswordAuthService authService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("정상적으로 토큰이 생성된다")
    void createToken_Success() {
        memberService.createMember(new MemberRequest(GithubResponses.사용자1.getEmail(), GithubResponses.DEFAULT_PASSWORD,
            GithubResponses.DEFAULT_AGE));

        TokenResponse tokenResponse = authService.login(
            TokenRequest.of(GithubResponses.사용자1.getEmail(), GithubResponses.DEFAULT_PASSWORD)
        );

        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 토큰을 생성하려고 하면 오류가 발생한다")
    void createToken_MemberNotFound() {
        String email = GithubResponses.PROFILE_없는_사용자.getEmail();
        String password = GithubResponses.DEFAULT_PASSWORD;

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> authService.login(TokenRequest.of(email, password)))
            .withMessageContaining(MEMBER_NOT_FOUND_MESSAGE);
    }

    @Test
    @DisplayName("틀린 비밀번호로 토큰 생성을 요청하면 오류가 발생한다")
    void createToken_InvalidPassword() {
        memberService.createMember(
            new MemberRequest(사용자3.getEmail(), GithubResponses.DEFAULT_PASSWORD, GithubResponses.DEFAULT_AGE));

        assertThatExceptionOfType(AuthenticationException.class)
            .isThrownBy(() ->
                authService.login(TokenRequest.of(사용자3.getEmail(), WRONG_PASSWORD))
            );
    }
}