package nextstep.subway.favorite.acceptance;

import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.*;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.*;
import static nextstep.subway.line.acceptance.step.LineStationAcceptanceStep.*;
import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "javajigi@email.com";
    public static final String PASSWORD = "nextstep";
    private TokenResponse tokenResponse;

    private Long lineId1;
    private Long lineId2;
    private Long lineId3;
    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long stationId4;

    /**
     * 교대역      -      강남역
     * |                 |
     * 남부터미널역           |
     * |                 |
     * 양재역      -       -|
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("교대역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("양재역");
        ExtractableResponse<Response> createdStationResponse4 = 지하철역_등록되어_있음("남부터미널");
        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_등록되어_있음("신분당선", "RED");
        ExtractableResponse<Response> createLineResponse3 = 지하철_노선_등록되어_있음("3호선", "ORANGE");

        lineId1 = createLineResponse1.as(LineResponse.class).getId();
        lineId2 = createLineResponse2.as(LineResponse.class).getId();
        lineId3 = createLineResponse3.as(LineResponse.class).getId();
        stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        stationId4 = createdStationResponse4.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록되어_있음(lineId1, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId1, stationId2, 2, 2);
        지하철_노선에_지하철역_등록되어_있음(lineId2, null, stationId2, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId2, stationId3, 2, 1);
        지하철_노선에_지하철역_등록되어_있음(lineId3, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId1, stationId4, 1, 2);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId4, stationId3, 2, 2);

        회원_등록되어_있음(EMAIL, PASSWORD, 20);

        // 로그인_되어있음
        tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        로그인_요청(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기 목록에서 역 하나를 추가한다.")
    @Test
    void 개인별_즐겨찾기를_추가한다() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(tokenResponse, stationId1, stationId4);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void 개인별_즐겨찾기_목록을_조회한다() {
        // given
        즐겨찾기_생성을_요청(tokenResponse, stationId1, stationId2);
        즐겨찾기_생성을_요청(tokenResponse, stationId3, stationId4);

        // when
        ExtractableResponse<Response> favoriteListResponse = 즐겨찾기_목록_조회_요청(tokenResponse);

        // then
        즐겨찾기_목록_조회됨(favoriteListResponse);
    }

    @DisplayName("즐겨찾기 목록에서 역 하나를 삭제한다.")
    @Test
    void 개인별_즐겨찾기를_삭제한다() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_등록되어_있음(tokenResponse, stationId1, stationId3);

        // when
        ExtractableResponse<Response> removeResponse = 즐겨찾기_삭제_요청(tokenResponse, createResponse);

        // then
        즐겨찾기_삭제됨(removeResponse);
    }
}
