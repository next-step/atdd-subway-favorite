package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp(){
        databaseCleanup.execute();
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
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(강남역, 양재역, 7));

        dataLoader.loadMemberData();
    }

    /**
     * When - 회원 토큰과 함께 즐겨찾기에 추가할 source station id / target station id를 요청으로 보내면
     * Then - 201 response를 반환한다.
     */
    @Test
    @DisplayName("즐겨찾기 생성 테스트")
    void createFavorite(){
        // given
        String token = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, 강남역, 남부터미널역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    /**
     * When - 토큰과 함께 조회 요청을 보내면
     * Then - 회원의 즐겨찾기 리스트를 반환한다.
     */
    @Test
    @DisplayName("즐겨찾기 조회 테스트")
    void showFavorites(){
        // given
        String token = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        즐겨찾기_생성_요청(token, 강남역, 남부터미널역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(token);


        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("source.id", Long.class)).contains(강남역),
                () -> assertThat(response.jsonPath().getList("target.id", Long.class)).contains(남부터미널역)
        );

    }

    /**
     * When - 토큰이 없이 조회 요청을 보내면
     * Then - Unauthorized 401 에러를 반환한다.
     */
    @Test
    @DisplayName("즐겨찾기 조회 실패: Token 인증 실패")
    void showFavorites_UnauthorizedFail(){
        // given

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청("NO TOKEN");

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getString("errorMessage")).isEqualTo("인증 토큰이 유효하지 않습니다."),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );
    }

    /**
     * When - 토큰과 즐겨찾기 id로 삭제 요청을 보내면
     * Then - 204 No Content를 응답한다.
     */
    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    void deleteFavorite(){
        // given
        String token = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        Long favoriteId = 즐겨찾기_생성_요청(token, 강남역, 남부터미널역).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(token, favoriteId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
