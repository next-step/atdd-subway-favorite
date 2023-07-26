package subway.acceptance.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;
import subway.utils.AcceptanceTest;
import subway.utils.GithubResponses;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder().email(EMAIL).password(PASSWORD).age(AGE).build();
        memberRepository.save(member);
    }


    @DisplayName("로그인에 성공하면 토큰이 발급된다")
    @Test
    void getTokenWithSuccessLogin() {
        // given
        var 로그인_요청 = AuthFixture.로그인_요청_만들기(EMAIL, PASSWORD);

        // when
        var response = AuthSteps.로그인_API(로그인_요청);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("비밀번호가 틀리면 로그인이 되지 않는다")
    @Test
    void failLoginWithInvalidPassword() {
        // when
        var 로그인 = AuthFixture.로그인_요청_만들기(EMAIL, "FAILED_PASSWORD");
        var 로그인_응답 = AuthSteps.로그인_API(로그인);

        // then
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("아이디가 틀리면 로그인이 되지 않는다")
    @Test
    void failLoginWithInvalidLoginEmail() {
        // when
        var 로그인 = AuthFixture.로그인_요청_만들기("FAKE_EMAIL", PASSWORD);
        var 로그인_응답 = AuthSteps.로그인_API(로그인);

        // then
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("위조 토큰으로 시도하는 인가는 실패한다")
    @Test
    void validTokenWithInvalidToken() {
        // when
        var response = AuthSteps.유효하지_않은_로그인_API();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("code로 GitHub 인증을 할 수 있다")
    @Test
    void githubAuth() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponses.사용자1.getCode());

        // when
        var response = AuthSteps.GITHUB_CODE_API(params);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
