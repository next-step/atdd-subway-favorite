package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.FavoritesSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class FavoritesAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 역삼역;

    @Override
    public void setUp() {
        super.setUp();
        회원_생성(ADMIN_EMAIL, PASSWORD, AGE);
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청("역삼역").jsonPath().getLong("id");
    }

    @DisplayName("로그인 안된 상태에서 즐겨찾기를 추가한다.")
    @Test
    void addFavorites_NotLogin() {
        // given
        final ExtractableResponse<Response> 결과 = 미로그인_즐겨찾기추가(강남역, 역삼역);

        // then
        로그인되지않은요청(결과);
    }

    private ExtractableResponse<Response> 미로그인_즐겨찾기추가(final Long source, final Long target) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("source", source, "target", target))
                .when().post("/favorites")
                .then().log().all().extract();
    }

    @DisplayName("로그인 된 상태에서 즐겨찾기를 추가한다.")
    @Test
    void addFavorites_Login() {
        // given
        final ExtractableResponse<Response> 결과 = 즐겨찾기추가(강남역, 역삼역);

        // then
        즐겨찾기추가성공(결과);
    }

    private void 즐겨찾기추가성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(즐겨찾기ID(response)).isNotNull();
    }

    private long 즐겨찾기ID(final ExtractableResponse<Response> response) {
        return 즐겨찾기조회_헤더(response).jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 즐겨찾기조회_헤더(final ExtractableResponse<Response> response) {
        return authGiven(관리자Bearer토큰())
                .given().log().all()
                .when().get(response.header("location"))
                .then().log().all().extract();
    }

    @DisplayName("로그인 안된 상태에서 즐겨찾기를 조회한다.")
    @Test
    void getFavorites_NotLogin() {
        // given
        final ExtractableResponse<Response> 결과 = 미로그인_즐겨찾기조회();

        // then
        로그인되지않은요청(결과);
    }

    private ExtractableResponse<Response> 미로그인_즐겨찾기조회() {
        return RestAssured
                .given().log().all()
                .when().get("/favorites/1")
                .then().log().all().extract();
    }

    @DisplayName("로그인 된 상태에서 즐겨찾기를 조회한다.")
    @Test
    void getFavorites_Login() {
        // given
        final Long 즐겨찾기ID = 즐겨찾기ID(즐겨찾기추가(강남역, 역삼역));

        final ExtractableResponse<Response> 결과 = 즐겨찾기조회(즐겨찾기ID);

        // then
        즐겨찾기조회성공(결과);
    }

    private void 즐겨찾기조회성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("id")).isNotNull();
    }


    @DisplayName("로그인 안된 상태에서 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorites_NotLogin() {
        // given
        final ExtractableResponse<Response> 결과 = 미로그인_즐겨찾기삭제();

        // then
        로그인되지않은요청(결과);
    }

    private ExtractableResponse<Response> 미로그인_즐겨찾기삭제() {
        return RestAssured
                .given().log().all()
                .when().delete("/favorites/1")
                .then().log().all().extract();
    }

    @DisplayName("로그인 된 상태에서 즐겨찾기를 조회한다.")
    @Test
    void deleteFavorites_Login() {
        // given
        final Long 즐겨찾기ID = 즐겨찾기ID(즐겨찾기추가(강남역, 역삼역));

        final ExtractableResponse<Response> 결과 = 즐겨찾기삭제(즐겨찾기ID);

        // then
        즐겨찾기삭제성공(결과, 즐겨찾기ID);
    }

    private void 즐겨찾기삭제성공(final ExtractableResponse<Response> response, final Long id) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(즐겨찾기조회(id).statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private void 로그인되지않은요청(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}