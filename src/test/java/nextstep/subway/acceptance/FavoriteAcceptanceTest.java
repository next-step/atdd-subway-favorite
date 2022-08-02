package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.exception.NotFoundStationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록_조회됨;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_성공;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_실패_확인;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.관리자_로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.유저_로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 없는역 = 1234L;

    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    private String accessToken;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        accessToken = 관리자_로그인_되어_있음();

        교대역 = 지하철역_생성_요청(accessToken, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(accessToken, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(accessToken, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(accessToken, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청(accessToken, "2호선", "green").jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청(accessToken, "3호선", "orange").jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청(accessToken, "신분당선", "red").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(accessToken, 이호선, createSectionCreateParams(교대역, 강남역, 10));
        지하철_노선에_지하철_구간_생성_요청(accessToken, 신분당선, createSectionCreateParams(강남역, 양재역, 3));
        지하철_노선에_지하철_구간_생성_요청(accessToken, 삼호선, createSectionCreateParams(교대역, 남부터미널역, 5));
        지하철_노선에_지하철_구간_생성_요청(accessToken, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 5));

        accessToken = 유저_로그인_되어_있음();
    }

    @DisplayName("즐겨찾기 생성에 성공한다")
    @Test
    public void create_favorite_success() {
        // when
        var response = 즐겨찾기_생성_요청(accessToken, 교대역, 강남역);

        // then
        즐겨찾기_생성_성공(response);
    }

    @DisplayName("비정상적인 시작, 도착 지점을 입력하면 예외를 발생시킨다")
    @Test
    public void create_favorite_fail_by_wrong_station() {
        // when
        var response = 즐겨찾기_생성_요청(accessToken, 없는역, 강남역);

        // then
        즐겨찾기_생성_실패_확인(response, HttpStatus.NOT_FOUND, NotFoundStationException.MESSAGE);
    }

    @DisplayName("모든 즐겨찾기를 성공적으로 조회한다")
    @Test
    public void search_all_favorites() {
        // given
        즐겨찾기_생성_요청(accessToken, 교대역, 강남역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        즐겨찾기_목록_조회됨(response);
    }

    @DisplayName(" 정상적인 즐겨찾기 번호를 입력하면 삭제 가능하다")
    @Test
    public void delete_favorite_success() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 교대역, 강남역);
        String location = createResponse.header("Location");

        // when
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .delete(location)
                .then().log().all()
                .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, Integer distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance.toString());
        return params;
    }
}
