package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceSteps.given;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 남부터미널역;
    private Long 양재역;

    private String 잘못된_토큰 = "wrong";

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
    }

    @Test
    @DisplayName("유저가 즐겨찾기를 관리한다.")
    void userManageFavorites() {
        var 생성_요청_결과 = 즐겨찾기_생성_요청(사용자, 교대역, 양재역);
        즐겨찾기_생성됨(생성_요청_결과);

        var 즐겨찾기_조회_정보 = 즐겨찾기_조회_요청(사용자, 생성_요청_결과);
        즐겨찾기_조회됨(즐겨찾기_조회_정보, 교대역, 양재역);

        즐겨찾기_생성_요청(사용자, 남부터미널역, 양재역);
        var 즐겨찾기_목록_조회_정보 = 즐겨찾기_목록_조회_요청(사용자);
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_정보, 2);

        var 즐겨찾기_삭제_결과 = 즐겨찾기_삭제_요청(사용자, 생성_요청_결과);
        즐겨찾기_삭제됨(즐겨찾기_삭제_결과);
        var 삭제된_즐겨찾기_조회_정보 = 즐겨찾기_조회_요청(사용자, 생성_요청_결과);
        찾을수없음(삭제된_즐겨찾기_조회_정보);
    }

    @Test
    @DisplayName("사용자, 관리자가 아닌 경우 즐겨찾기를 관리할 수 없다.")
    void invalidUserManageFavorites() {
        // given
        var 생성된_즐겨찾기_결과 = 즐겨찾기_생성_요청(사용자, 교대역, 양재역);

        var 생성_요청_결과 = 즐겨찾기_생성_요청(잘못된_토큰, 교대역, 양재역);
        권한이_없음(생성_요청_결과);

        var 즐겨찾기_조회_정보 = 즐겨찾기_조회_요청(잘못된_토큰, 생성된_즐겨찾기_결과);
        권한이_없음(즐겨찾기_조회_정보);

        var 즐겨찾기_목록_조회_정보 = 즐겨찾기_목록_조회_요청(잘못된_토큰);
        권한이_없음(즐겨찾기_목록_조회_정보);

        var 즐겨찾기_삭제_결과 = 즐겨찾기_삭제_요청(잘못된_토큰, 생성된_즐겨찾기_결과);
        권한이_없음(즐겨찾기_삭제_결과);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return given(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return given(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return given(accessToken)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response, Long source, Long target) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("source.id")).isEqualTo(source);
        assertThat(response.jsonPath().getLong("target.id")).isEqualTo(target);
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, int favoriteCount) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id", Long.class).size()).isEqualTo(favoriteCount);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 찾을수없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private void 권한이_없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
