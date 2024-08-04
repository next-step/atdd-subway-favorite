package nextstep.member.acceptance;

import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.AuthSteps.*;
import static nextstep.member.acceptance.MemberSteps.*;
import static nextstep.fake.github.GithubStaticUsers.깃헙_사용자1;
import static nextstep.subway.acceptance.step.BaseStepAsserter.응답_상태값이_올바른지_검증한다;

class AuthAcceptanceTest extends BaseTestSetup {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    /**
     * Given: 회원가입이 이미 된 회원이
     * When: 이메일 패스워드 방식으로 로그인을 하면
     * Then: 인증 토큰이 반환되고 내 정보를 가져올 수 있다.
     */
    @Test
    void 이메일_패스워드_로그인_테스트() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var 로그인_응답값 = 이메일_패스워드_로그인(EMAIL, PASSWORD);

        // then
        응답_상태값이_올바른지_검증한다(로그인_응답값, HttpStatus.OK.value());

        // then
        var 인증토큰 = 인증토큰을_추출한다(로그인_응답값);
        회원_정보_조회됨(내_정보_조회_요청(인증토큰), EMAIL, AGE);
    }

    /**
     * Given: 등록되지 않은 회원이
     * When: 깃헙 로그인을 요청하면
     * Then: 인증 토큰이 반환되고 내 정보를 가져올 수 있다.
     */
    @Test
    void 깃헙_회원가입_테스트() {
        // when
        var 로그인_응답값 = 깃헙_로그인(깃헙_사용자1);

        // then
        응답_상태값이_올바른지_검증한다(로그인_응답값, HttpStatus.OK.value());

        // then
        var 인증토큰 = 인증토큰을_추출한다(로그인_응답값);
        깃헙_사용자_조회됨(내_정보_조회_요청(인증토큰), 깃헙_사용자1);
    }

    /**
     * Given 등록된 회원이
     * When: 깃헙 로그인을 하면
     * Then: 인증 토큰이 반환되고 내 정보를 가져올 수 있다.
     */
    @Test
    void 깃헙_로그인_테스트() {
        // given
        회원_생성_요청(깃헙_사용자1.getEmail(), PASSWORD, AGE);

        // when
        var 로그인_응답값 = 깃헙_로그인(깃헙_사용자1);

        // then
        응답_상태값이_올바른지_검증한다(로그인_응답값, HttpStatus.OK.value());

        // then
        var 인증토큰 = 인증토큰을_추출한다(로그인_응답값);
        회원_정보_조회됨(내_정보_조회_요청(인증토큰), 깃헙_사용자1.getEmail(), AGE);
    }
}