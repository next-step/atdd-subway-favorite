package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능")
class FavoriteAcceptanceTest extends AcceptanceTest{
    private static final String 이메일 = "email@email.com";
    private static final String 비밀번호 = "password";
    public static final int 나이 = 20;
    String 로그인_토큰;
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 출발역;
    private Long 도착역;
    /**
     * 초기 맴버, 역 노선 세팅
     *
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_요청(이메일, 비밀번호, 나이);

        ExtractableResponse<Response> bearerResponse = 베어러_인증_로그인_요청(이메일, 비밀번호);
        TokenResponse tokenResponse = bearerResponse.as(TokenResponse.class);
        로그인_토큰 = tokenResponse.getAccessToken();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
    }

    /**
     * given 출발역과 도착역이 주어지고
     * when 즐겨찾기를 추가하면
     * then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기 추가 기능")
    @Test
    void createFavorites() {
        //given
        출발역 = 교대역;
        도착역 = 양재역;

        //when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_추가(로그인_토큰,출발역 + "", 도착역 + "");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void findFavorites() {

    }

    @Test
    void deleteFavorites() {

    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
