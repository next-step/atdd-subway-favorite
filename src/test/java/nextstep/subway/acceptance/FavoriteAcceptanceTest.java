package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private Long 강남역;
    private Long 양재역;
    String accessToken;

    /**
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        지하철_노선_등록되어_있음(강남역, 양재역);
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("권한이 없는 경우 즐겨찾기 관리 요청")
    void manageFavoriteWhenUnauthorized() {
        String invalidToken = "";

        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(invalidToken, 강남역, 양재역);
        권한_없음(createResponse);

        ExtractableResponse<Response> fineResponse = 즐겨찾기_목록_조회_요청(invalidToken);
        권한_없음(fineResponse);

        ExtractableResponse<Response> createSuccessResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 양재역);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(invalidToken, createSuccessResponse);
        권한_없음(deleteResponse);
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @Test
    @DisplayName("즐겨찾기를 관리한다.")
    void manageFavorite() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 양재역);
        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> fineResponse = 즐겨찾기_목록_조회_요청(accessToken);
        즐겨찾기_목록_조회됨(fineResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse);
        즐겨찾기_삭제됨(deleteResponse);
    }
}
