package nextstep.auth.acceptance;

import static nextstep.auth.acceptance.AuthSteps.깃허브_로그인_요청;
import static nextstep.auth.acceptance.AuthSteps.일반_로그인_요청;
import static nextstep.auth.acceptance.GithubUserFixture.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Given 회원 가입을 하고
     * When 일반 로그인을 하면
     * Then 액세스 토큰을 발급 해준다
     */
    @Test
    void 일반_로그인을_한다() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when
        var response = 일반_로그인_요청(EMAIL, PASSWORD);

        // then
        응답_상태코드_검증(response, HttpStatus.OK);
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When 회원가입이 되어 있지 않은 회원 정보로 일반 로그인을 하면
     * Then 요청이 실패된다
     */
    @Test
    void 일반_로그인_시_회원가입이_되어_있지_않은_회원_정보이면_요청이_실패된다() {
        // when
        var response = 일반_로그인_요청(EMAIL, PASSWORD);

        // then
        응답_상태코드_검증(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * When 깃 허브 로그인을 하면
     * Then 액세스 토큰을 발급 해준다
     */
    @Test
    void 깃허브_로그인을_한다() {
        // when
        var response = 깃허브_로그인_요청(사용자1.getCode());

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When 등록이 되어 있지 않은 코드로 깃허브 로그인을 하면
     * Then 요청이 실패된다
     */
    @Test
    void 깃허브_로그인_시_등록이_되어_있지_않은_코드이면_요청이_실패된다() {
        // when
        var response = 깃허브_로그인_요청("Not Register Code");

        // then
        응답_상태코드_검증(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void 응답_상태코드_검증(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
