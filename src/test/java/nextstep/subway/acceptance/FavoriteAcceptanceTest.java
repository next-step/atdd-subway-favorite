package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 양재역;
    private Long 분당역;
    private Long 신림역;
    private String 즐겨찾는_구간;

    /**
     * Given 지하철역 생성 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        분당역 = 지하철역_생성_요청("분당역").jsonPath().getLong("id");
        신림역 = 지하철역_생성_요청("신림역").jsonPath().getLong("id");
        즐겨찾는_구간 = 즐겨찾기_등록_요청(회원_사용자_요청(), 분당역, 신림역).jsonPath().getString("id");
    }

    /**
     * Given : 로그인한 사용자가
     * Then  : 즐겨찾기에 출발역과 도착역을 등록합니다.
     */
    @Test
    void addFavorite() {
        // given
        RequestSpecification 로그인한_사용자 = 회원_사용자_요청();

        //when
        ExtractableResponse<Response> 로그인한_사용자_즐겨찾기_등록 = 즐겨찾기_등록_요청(로그인한_사용자, 강남역, 양재역);

        //then
        assertThat(로그인한_사용자_즐겨찾기_등록.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    /**
     * Given : 로그인하지 않은 사용자가
     * When  : 즐겨찾기를 추가하려하면
     * Then : 401 Unauthorized 응답을 받습니다.
     */
    @Test
    void addFavorite_unauthorized() {
        // given
        RequestSpecification 로그인하지_않은_사용자 = 미회원_사용자_요청();

        // when
        ExtractableResponse<Response> 로그인하지_않은_사용자_즐겨찾기_등록 = 즐겨찾기_등록_요청(로그인하지_않은_사용자, 강남역, 양재역);

        // then
        assertThat(로그인하지_않은_사용자_즐겨찾기_등록.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


    /**
     * Given : 로그인한 사용자가
     * When  : 즐겨찾기에 출발역과 도착역을 등록하면
     * Then : 자신의 즐겨찾기 목록에서 찾을 수 있습니다.
     */
    @Test
    void getFavorites() {
        // given
        ExtractableResponse<Response> 로그인한_사용자 = 즐겨찾기_등록_요청(회원_사용자_요청(), 강남역, 양재역);

        //when
        ExtractableResponse<Response> 로그인한_사용자_즐겨찾기_등록 = 로그인한_사용자;
        assertThat(로그인한_사용자_즐겨찾기_등록.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> 로그인한_사용자_즐겨찾기_조회 = 즐겨찾기_조회_요청(회원_사용자_요청());
        assertThat(로그인한_사용자_즐겨찾기_조회.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    /**
     * Given : 로그인하지 않은 사용자가
     * When  : 즐겨찾기를 조회하려하면
     * Then : 401 Unauthorized 응답을 받습니다.
     */
    @Test
    void getFavorites_unauthorized() {
        RequestSpecification 로그인하지_않은_사용자 = 미회원_사용자_요청();

        ExtractableResponse<Response> 로그인하지_않은_사용자_즐겨찾기_조회 = 즐겨찾기_조회_요청(로그인하지_않은_사용자);

        assertThat(로그인하지_않은_사용자_즐겨찾기_조회.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given : 로그인한 사용자가
     * When  : 즐겨찾기에 출발역과 도착역을 등록하고
     * Then : 즐겨찾기를 삭제합니다.
     */
    @Test
    void deleteFavorite() {
        // given
        RequestSpecification 로그인한_사용자 = 회원_사용자_요청();

        // when
        ExtractableResponse<Response> 로그인한_사용자_즐겨찾기_등록 = 즐겨찾기_등록_요청(로그인한_사용자, 강남역, 양재역);
        assertThat(로그인한_사용자_즐겨찾기_등록.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        String 즐겨찾기 = 로그인한_사용자_즐겨찾기_등록.jsonPath().getString("id");
        ExtractableResponse<Response> 로그인한_사용자_즐겨찾기_삭제 = 즐겨찾기_삭제_요청(로그인한_사용자, 즐겨찾기);
        assertThat(로그인한_사용자_즐겨찾기_삭제.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    /**
     * Given : 로그인하지 않은 사용자가
     * When  : 즐겨찾기를 삭제하려하면
     * Then : 401 Unauthorized 응답을 받습니다.
     */
    @Test
    void deleteFavorite_unauthorized() {
        // given
        RequestSpecification 로그인하지_않은_사용자 = 미회원_사용자_요청();

        // when
        ExtractableResponse<Response> 로그인하지_않은_사용자_즐겨찾기_삭제 = 즐겨찾기_삭제_요청(로그인하지_않은_사용자, 즐겨찾는_구간);

        // then
        assertThat(로그인하지_않은_사용자_즐겨찾기_삭제.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static RequestSpecification 회원_사용자_요청() {
        return RestAssured.given().log().all()
            .auth().oauth2(로그인_되어_있음("member@email.com", "password"));
    }

    private static RequestSpecification 미회원_사용자_요청() {
        return RestAssured.given().log().all();
    }

    private ExtractableResponse<Response> 즐겨찾기_등록_요청(RequestSpecification specification, long source, long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        return specification
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(RequestSpecification specification) {
        return specification
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(RequestSpecification specification, String id) {
        return specification.when().delete("/favorites/{id}", id)
            .then().log().all().extract();
    }
}
