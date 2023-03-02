package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp(){
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green" ).jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청("신분당선", "red").jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("3호선", "orange").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(교대역, 남부터미널역, 5));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(교대역, 강남역, 6));
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(남부터미널역, 양재역, 7));
    }

    /**
     * When - 회원 토큰과 함께 즐겨찾기에 추가할 source station id / target station id를 요청으로 보내면
     * Then - 201 response를 반환한다.
     */
    @Test
    @DisplayName("즐겨찾기 생성 테스트")
    void createFavorite(){

    }

    /**
     * When - 토큰과 함께 조회 요청을 보내면
     * Then - 회원의 즐겨찾기 리스트를 반환한다.
     */
    @Test
    @DisplayName("즐겨찾기 조회 테스트")
    void showFavorites(){

    }

    /**
     * When - 토큰이 없이 조회 요청을 보내면
     * Then - Unauthorized 401 에러를 반환한다.
     */
    @Test
    @DisplayName("즐겨찾기 조회 실패: Token 인증 실패")
    void showFavorites__UnauthorizedFail(){

    }

    /**
     * When - 토큰과 즐겨찾기 id로 삭제 요청을 보내면
     * Then - 204 No Content를 응답한다.
     */
    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    void deleteFavorite(){

    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
