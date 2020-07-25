package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.*;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineStationAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.로그인_되어_있음;
import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.회원_등록되어_있음;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;


@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

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
    }

    /**
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // 즐겨찾기 생성을 요청
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(stationId1, stationId2);

        // then
        즐겨찾기_생성됨(createResponse);

        // 즐겨찾기 목록 조회 요청
        // when
        ExtractableResponse<Response> findFavoritesResponse = 즐겨찾기_목록_조회_요청();

        // then
        즐겨찾기_목록_조회됨(findFavoritesResponse);

        // 즐겨찾기 삭제 요청
        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse);

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("내 즐겨찾기를 관리한다.")
    @Test
    void manageMyFavorite() {
        // given
        TokenResponse loginToken = 로그인_되어_있음(EMAIL, PASSWORD);
        Map<String, String> createFavoriteMap = new HashMap<>();
        createFavoriteMap.put("source", stationId1 + "");
        createFavoriteMap.put("target", stationId2 + "");

        // when
        ExtractableResponse<Response> createResponse = 내_즐겨찾기_생성_요청(loginToken, createFavoriteMap);

        // then
        즐겨찾기_생성됨(createResponse);

        // 즐겨찾기 목록 조회 요청
        // when
        ExtractableResponse<Response> findMyFavoritesResponse = 내_즐겨찾기_목록_조회_요청(loginToken);

        // then
        즐겨찾기_목록_조회됨(findMyFavoritesResponse);

        // 즐겨찾기 삭제 요청
        // when
        ExtractableResponse<Response> deleteResponse = 내_즐겨찾기_삭제_요청(loginToken, createResponse);

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }
}
