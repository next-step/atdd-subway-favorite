package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.AuthSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final String INVALID_PASSWORD = "asdfafasdfasdf";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        expectHttpStatus(response, HttpStatus.OK);
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Bearer Auth Fail")
    @Test
    void bearerAuthFail() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, INVALID_PASSWORD);

        expectHttpStatus(response, HttpStatus.UNAUTHORIZED);
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        Map<String, String> params = 깃허브_인증_로그인_요청_파라미터_생성();

        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(params);

        expectHttpStatus(response, HttpStatus.OK);
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    public static Map<String, String> 깃허브_인증_로그인_요청_파라미터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponses.사용자1.getCode());
        return params;
    }

    @DisplayName("Github Auth fail")
    @Test
    void githubAuthFail() {
        Map<String, String> params = new HashMap<>();
        params.put("code", "code");

        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(params);

        expectHttpStatus(response, HttpStatus.UNAUTHORIZED);
    }


    // 로그인 인증이 필요한 사이트에 접근 가능여부 테스트가 목적이기 떄문에 인증관련 인수테스트 클래스에 위치해야한다

    /**
     * When  : 로그인하지 않은 사용자가 내정보 조회를 요청하면
     * Then  : 요청이 거부된다 (상세하게 401 응답을 리턴한다 이렇게 정의하는게 좋을까?)
     */
    @DisplayName("로그인 하지 않은 사용자는 내정보 조회 불가")
    @Test
    void myInfoUnauthorized() {
        ExtractableResponse<Response> response = 단순_조회_요청("/members/me");

        expectHttpStatus(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * When  : 로그인하지 않은 사용자가 즐겨찾기 관련기능을 요청하면
     * Then  : 요청이 거부된다
     */
    @DisplayName("로그인 하지 않은 사용자는 즐겨찾기 기능 사용불가")
    @Test
    void favoriteUnauthorized() {
        ExtractableResponse<Response> response = 단순_조회_요청("/favorites");

        expectHttpStatus(response, HttpStatus.UNAUTHORIZED);
    }

    private ExtractableResponse<Response> 단순_조회_요청(String path) {
        return RestAssured
                .given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    private void expectHttpStatus(ExtractableResponse<Response> response, HttpStatus unauthorized) {
        assertThat(response.statusCode()).isEqualTo(unauthorized.value());
    }
}