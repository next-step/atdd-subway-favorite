package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.request.GetAccessTokenRequest;
import nextstep.member.application.response.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.common.Constant.*;
import static nextstep.member.acceptance.AuthAcceptanceStep.*;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Given 회원가입 후 로그인을 하여 받은 토큰으로
     * When 사용자 정보를 조회하면
     * Then 사용자 정보를 조회할 수 있다.
     */
    @DisplayName("발급된 토큰으로 사용자를 조회할 수 있다.")
    @Test
    void 발급된_토큰으로_조회() {
        // given
        회원_생성_요청(임꺽정_이메일, 임꺽정_비밀번호, 임꺽정_나이);
        String accessToken = 로그인_성공(임꺽정_이메일, 임꺽정_비밀번호);

        // thwn
        MemberResponse 사용자_조회_응답 = 사용자_조회됨(accessToken);
        String 사용자_이메일 = 사용자_조회_응답.getEmail();

        // then
        assertThat(사용자_이메일).isEqualTo(임꺽정_이메일);
    }

    /**
     * When 등록된 코드로 토큰을 요청하면
     * Then 토큰을 발급 받는다.
     */
    @DisplayName("등록된 코드로 토큰을 요청하면 토큰을 발급 받는다.")
    @Test
    void 깃허브_로그인을_성공() {
        // then
        String 사용자1_코드 = GithubResponses.사용자_홍길동.getCode();

        // when
        String 토큰 = 깃허브_로그인_성공(사용자1_코드);

        // then
        깃허브_로그인_성공_검증(토큰, GithubResponses.사용자_홍길동);
    }

    /**
     * When 등록되지 않은 코드로 토큰을 요청하면
     * Then 토큰을 발급받지 못한다.
     */
    @DisplayName("등록되지 않은 코드로 토큰을 요청하면 토큰을 발급받지 못한다.")
    @Test
    void 깃허브_로그인을_실패() {
        // then
        String 미등록_사용자_코드 = "qwer1234";
        GetAccessTokenRequest 깃허브_로그인_요청 = GetAccessTokenRequest.from(미등록_사용자_코드);

        // when & then
        깃허브_로그인_실패_검증(깃허브_로그인_시도(깃허브_로그인_요청), HttpStatus.UNAUTHORIZED);
    }

    void 깃허브_로그인_성공_검증(String accessToken, GithubResponses githubResponses) {
        assertThat(accessToken).isNotBlank();
        assertThat(accessToken).isEqualTo(githubResponses.getAccessToken());
    }

    void 깃허브_로그인_실패_검증(ExtractableResponse<Response> extractableResponse, HttpStatus status) {
        assertThat(extractableResponse.statusCode()).isEqualTo(status.value());
    }

}
