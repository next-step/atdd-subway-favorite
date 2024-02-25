package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.request.GetAccessTokenRequest;
import nextstep.member.application.response.MemberResponse;
import nextstep.member.test.GithubResponses;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.common.Constant.임꺽정_이메일;
import static nextstep.member.acceptance.AuthAcceptanceStep.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

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

    /**
     * Given 회원 가입을 하지않고 로그인을 할 경우
     * When 깃허브 토큰이 정상적으로 발급되면
     * Then 회원가입을 진행한다.
     */
    @DisplayName("회원가입을 하지않고 로그인을 할 경우 깃허브 토큰이 정상적으로 발급되면 회원가입을 진행한다.")
    @Test
    void 회원가입하지않고_깃허브_로그인을_하면_회원가입() {
        // given
        String accessToken = 깃허브_로그인_성공(GithubResponses.사용자_임꺽정.getCode());

        // thwn
        MemberResponse 사용자_조회_응답 = 사용자_조회됨(accessToken);
        String 사용자_이메일 = 사용자_조회_응답.getEmail();

        // then
        assertThat(사용자_이메일).isEqualTo(임꺽정_이메일);
    }

    void 깃허브_로그인_성공_검증(String accessToken, GithubResponses githubResponses) {
        assertThat(accessToken).isNotBlank();
        assertThat(accessToken).isEqualTo(githubResponses.getAccessToken());
    }

    void 깃허브_로그인_실패_검증(ExtractableResponse<Response> extractableResponse, HttpStatus status) {
        assertThat(extractableResponse.statusCode()).isEqualTo(status.value());
    }

}
