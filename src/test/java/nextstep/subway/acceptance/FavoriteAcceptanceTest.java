package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import nextstep.argumentResolver.InvalidValueException;
import nextstep.subway.utils.DatabaseCleanup;

public class FavoriteAcceptanceTest extends AcceptanceTest{

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 역삼역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;


    /**
     * 교대역    --- *2호선* ---   강남역 --- *2호선* --- 역삼역
     * |                           |
     * *3호선*                   *신분당선*
     * |                           |
     * 남부터미널역  --- *3호선* ---   양재  --- *3호선* ---   매봉
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

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(교대역, 남부터미널역));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(교대역, 강남역));
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(강남역, 양재역));

        dataLoader.loadMemberData(EMAIL, PASSWORD, AGE);
    }
    @Test
    void 즐겨찾기_생성_테스트() {
        // given
        String 토큰 = ACCESS_TOKEN_발급(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_API(토큰, 강남역, 교대역);

        // then
        assertEquals(response.statusCode(), HttpStatus.CREATED.value());

    }

    @Test
    void 즐겨찾기_생성_예외_테스트() {

        // given
        String 토큰 = ACCESS_TOKEN_발급(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_API(토큰, 강남역, 역삼역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 즐겨찾기_조회_테스트() {

    }

    @Test
    void 즐겨찾기_삭제_테스트() {

    }

    @Test
    void 비인증_유저_즐겨찾기_예외_테스트() {

    }

    private static String ACCESS_TOKEN_발급(String email, String password) {
        return 베어러_인증_로그인_요청(email, password).jsonPath().getString("accessToken");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", 6 + "");
        return params;
    }

}
