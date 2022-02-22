package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_권한이_없음;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_삭제됨;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_생성됨;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_정보_조회됨;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 기능")
public class FavoritesAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private String 로그인토큰;


    /**
     * 교대역    --- *2호선* ---   강남역 |                        | *3호선*                   *신분당선* |
     * | 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        로그인토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기 추가 하기")
    @Test
    public void addFavorite() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(로그인토큰, 강남역, 양재역);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기 조회 하기")
    @Test
    public void getFavorites() {
        // given
        즐겨찾기_생성_요청(로그인토큰, 강남역, 양재역);
        즐겨찾기_생성_요청(로그인토큰, 양재역, 교대역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(로그인토큰);

        // then
        즐겨찾기_정보_조회됨(response, new Long[]{강남역, 양재역}, new Long[]{양재역, 교대역});
    }

    @DisplayName("즐겨찾기 삭제 하기")
    @Test
    public void deleteFavorites() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(로그인토큰, 강남역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(로그인토큰, createResponse);

        // then
        즐겨찾기_삭제됨(response);
    }

    @DisplayName("즐겨찾기 추가 하기 - 유효하지 않은 토큰 일 경우")
    @Test
    public void addFavoriteWithInvalidToken() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청("invalid_token", 강남역, 양재역);

        // then
        즐겨찾기_권한이_없음(response);
    }

    @DisplayName("즐겨찾기 목록을 관리한다.")
    @Test
    public void manageFavorites() {
        // when 즐겨찾기 생성을 요청
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인토큰, 강남역, 양재역);
        // then 즐겨찾기 생성됨
        즐겨찾기_생성됨(즐겨찾기_생성_응답);

        // when 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(로그인토큰);
        // then 즐겨찾기 목록 조회됨
        즐겨찾기_정보_조회됨(즐겨찾기_목록_조회_응답, new Long[]{ 강남역 }, new Long[]{ 양재역 });

        // when 즐겨찾기 삭제 요청
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(로그인토큰, 즐겨찾기_생성_응답);
        // then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }


    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation,
        int distance) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId,
        int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }

}
