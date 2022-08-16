package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;

    private Long 이호선;
    private Long 삼호선;
    private Long 신분당선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    void setup() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역", adminAccessToken).jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역", adminAccessToken).jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역", adminAccessToken).jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역", adminAccessToken).jsonPath().getLong("id");

        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, adminAccessToken);
        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, adminAccessToken);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 5, adminAccessToken);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3), adminAccessToken);
    }

    /**
     * Given 지하철역을 등록하고,노선을 등록한다.
     * When 즐겨찾기 등록을 요청한다.
     * Then 즐겨찾기가 등록된다
     */
    @DisplayName("즐겨찾기 등록")
    @Test
    void registerFavorite() {

        // when (강남역 -> 양재역)
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(강남역, 양재역, memberAccessToken);

        // then
        assertThat(즐겨찾기_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철역을 등록하고,노선을 등록한다.
     * When 즐겨찾기 등록을 요청한다.
     * Then 권한이 없어 (401)이 리턴된다.
     */
    @DisplayName("즐겨찾기 등록 - 유효하지 않을경우")
    @Test
    void registerFavoriteIfInValidToken() {

        // when (강남역 -> 양재역)
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(강남역, 양재역, memberAccessToken+"attd");

        // then
        assertThat(즐겨찾기_등록_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 지하철역을 등록하고,노선을 등록한다.
     * When 즐겨찾기 등록을 요청한다.
     * Then 권한이 없어 (401)이 리턴된다.
     */
    @DisplayName("즐겨찾기 등록 - 비로그인 상태")
    @Test
    void registerFavoriteIfNotLogin() {

        // when (강남역 -> 양재역)
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(강남역, 양재역);

        // then
        assertThat(즐겨찾기_등록_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기가 등록된다.
     * When 즐겨찾기를 조회 요청한다.
     * Then 즐겨찾기 리스트가 조회된다.
     */
    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {

        // given (교대역 -> 양재역)
        즐겨찾기_등록_요청(교대역, 양재역, memberAccessToken);
        즐겨찾기_등록_요청(강남역, 남부터미널역, memberAccessToken);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(memberAccessToken);

        // then
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(즐겨찾기_조회_응답.jsonPath().getList("source.name", String.class)).containsExactly("교대역", "강남역");
        assertThat(즐겨찾기_조회_응답.jsonPath().getList("target.name", String.class)).containsExactly("양재역", "남부터미널역");
    }

    /**
     * Given 즐겨찾기가 등록된다.
     * When 즐겨찾기를 조회 요청한다.
     * Then 권한이 없어 (401)이 리턴된다.
     */
    @DisplayName("즐겨찾기 조회 - 유효하지 않을경우")
    @Test
    void getFavoritesIfInValidToken() {

        // given (교대역 -> 양재역)
        String inValidMemberAccessToken = memberAccessToken + "_attd";
        즐겨찾기_등록_요청(교대역, 양재역, memberAccessToken);
        즐겨찾기_등록_요청(강남역, 남부터미널역, memberAccessToken);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(inValidMemberAccessToken);

        // then
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기가 등록된다.
     * When 즐겨찾기를 조회 요청한다.
     * Then 권한이 없어 (401)이 리턴된다.
     */
    @DisplayName("즐겨찾기 조회 - 유효하지 않을경우")
    @Test
    void getFavoritesIfNotLogin() {

        // given (교대역 -> 양재역)
        즐겨찾기_등록_요청(교대역, 양재역, memberAccessToken);
        즐겨찾기_등록_요청(강남역, 남부터미널역, memberAccessToken);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청();

        // then
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기가 등록된다.
     * When 즐겨찾기를 삭제 요청한다.
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {

        // given
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(교대역, 양재역, memberAccessToken);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(즐겨찾기_등록_응답.header("location"), memberAccessToken);

        // then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 즐겨찾기가 등록된다.
     * When 즐겨찾기를 삭제 요청한다.
     * Then 권한이 없어 (401)이 리턴된다.
     */
    @DisplayName("즐겨찾기 삭제 - 유효하지 않을경우")
    @Test
    void deleteFavoriteIfInValidToken() {

        // given
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(교대역, 양재역, memberAccessToken);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(즐겨찾기_등록_응답.header("location"), memberAccessToken+"_attd");

        // then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기가 등록된다.
     * When 즐겨찾기를 삭제 요청한다.
     * Then 권한이 없어 (401)이 리턴된다.
     */
    @DisplayName("즐겨찾기 삭제 - 비로그인 상태")
    @Test
    void deleteFavoriteIfNotLogin() {

        // given
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(교대역, 양재역, memberAccessToken);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(즐겨찾기_등록_응답.header("location"));

        // then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


}
