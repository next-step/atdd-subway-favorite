package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteAcceptanceSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberFixture.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static Long 일호선;
    public static Long 이호선;
    public static Long 삼호선;
    public static Long 신분당선;

    public static Long 시청역;
    public static Long 서울역;
    public static Long 용산역;
    public static Long 교대역;
    public static Long 강남역;
    public static Long 역삼역;
    public static Long 고속터미널역;
    public static Long 남부터미널역;
    public static Long 양재역;
    public static Long 판교역;
    public static String 로그인_토큰;

    /**
     * 시청--서울--용산 (1호선)
     * <p>
     * 고속터미널 (3호선)
     * |
     * 교대--------강남---------역삼 (2호선)
     * |           |
     * 남부터미널----양재
     *             |
     *            판교 (신분당선)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        시청역 = 지하철역_생성_요청("시청역").jsonPath().getLong("id");
        서울역 = 지하철역_생성_요청("서울역").jsonPath().getLong("id");
        용산역 = 지하철역_생성_요청("용산역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청("역삼역").jsonPath().getLong("id");
        고속터미널역 = 지하철역_생성_요청("고속터미널역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");

        일호선 = 지하철_노선_생성_요청("일호선", "blue").jsonPath().getLong("id");
        이호선 = 지하철_노선_생성_요청("이호선", "green").jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("삼호선", "orange").jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청("신분당선", "red").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(일호선, createSectionParams(시청역, 서울역, 10));
        지하철_노선에_지하철_구간_생성_요청(일호선, createSectionParams(서울역, 용산역, 10));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionParams(교대역, 강남역, 10));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionParams(강남역, 역삼역, 10));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionParams(고속터미널역, 교대역, 10));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionParams(강남역, 남부터미널역, 10));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionParams(남부터미널역, 양재역, 10));
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionParams(강남역, 양재역, 5));
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionParams(양재역, 판교역, 10));

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        로그인_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("지하철 노선 즐겨찾기 관리")
    @Test
    void manageMyFavorites() {
        //given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String 로그인_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인_토큰, 판교역, 고속터미널역);
        즐겨찾기_생성됨(즐겨찾기_생성_응답);

        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(로그인_토큰);
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답, 1);

        Long 첫_번째_즐겨찾기 = 첫_번째_즐겨찾기(즐겨찾기_목록_조회_응답);
        ExtractableResponse<Response> 즐겨찾기_목록_삭제_응답 = 즐겨찾기_삭제_요청(로그인_토큰, 첫_번째_즐겨찾기);
        즐겨찾기_삭제됨(즐겨찾기_목록_삭제_응답, 0);
    }

    @DisplayName("비정상 경로 즐겨찾기 생성 예외")
    @Test
    void invalidPathFavoriteException() {
        //when
        ThrowingCallable 즐겨찾기_생성_시도 = () -> 즐겨찾기_생성_요청(로그인_토큰, 판교역, 서울역);

        //then
        즐겨찾기_생성_실패됨(즐겨찾기_생성_시도);
    }

    @DisplayName("존재하지 않은 즐겨찾기 삭제 예외")
    @Test
    void notExistFavoriteException() {
        //when
        ThrowingCallable 즐겨찾기_삭제_시도 = () -> 즐겨찾기_삭제_요청(로그인_토큰, 99L);

        //then
        즐겨찾기_삭제_실패됨(즐겨찾기_삭제_시도);
    }

    @DisplayName("비로그인 상태에서 즐겨찾기 관리 예외")
    @Test
    void manageMyFavoriteUnauthorizedException() {
        //given
        String 유효하지_않은_토큰 = "invalid token";

        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(유효하지_않은_토큰, 판교역, 고속터미널역);
        권한_없음(즐겨찾기_생성_응답);

        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(유효하지_않은_토큰);
        권한_없음(즐겨찾기_목록_조회_응답);

        ExtractableResponse<Response> 즐겨찾기_목록_삭제_응답 = 즐겨찾기_삭제_요청(유효하지_않은_토큰, 1L);
        권한_없음(즐겨찾기_목록_삭제_응답);
    }

    private Map<String, String> createSectionParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
