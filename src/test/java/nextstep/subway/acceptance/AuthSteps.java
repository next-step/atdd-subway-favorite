package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static nextstep.subway.acceptance.MemberSteps.OAuth2_인증으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;


public class AuthSteps {

    public static void Access_Token_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("accessToken")).isNotNull();
    }

    public static void 회원가입이_완료됨(String accessToken, String email) {
        var OAuth2_인증으로_내_회원_정보_조회_응답 = OAuth2_인증으로_내_회원_정보_조회_요청(accessToken);
        회원_정보_조회됨(OAuth2_인증으로_내_회원_정보_조회_응답, email, 0);
    }
}
