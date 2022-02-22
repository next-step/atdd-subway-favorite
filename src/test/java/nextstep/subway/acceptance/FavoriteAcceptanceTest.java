package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private String accessToken;
    private Long sourceId;
    private Long targetId;

    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        sourceId = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        targetId = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        지하철_노선_생성_요청("2호선", "green", sourceId, targetId, 10);

        final String EMAIL = "email@email.com";
        final String PASSWORD = "password";
        final int AGE = 20;
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    /**
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
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, sourceId, targetId);
        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> showResponse = 즐겨찾기_목록_조회_요청(accessToken);
        즐겨찾기_목록_조회됨(showResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse);
        즐겨찾기_삭제됨(deleteResponse);
    }

    /**
     * Given 다른 회원 생성 요청
     * And 다른 회원 로그인 되어 있음
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 다른 회원으로 즐겨찾기 삭제 요청
     * Then 권한 없음 오류 반환됨
     */
    @DisplayName("다른 회원의 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavoriteOfOtherMember() {
        // given
        final String otherEmail = "otherEmail@email.com";
        final String otherPassword = "otherPassword";
        final int otherAge = 20;
        회원_생성_요청(otherEmail, otherPassword, otherAge);
        final String otherAccessToken = 로그인_되어_있음(otherEmail, otherPassword);

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, sourceId, targetId);
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(otherAccessToken, createResponse);
        // then
        권한_없음_오류_반환됨(deleteResponse);
    }

    /**
     * When 로그인 없이 즐겨찾기 생성을 요청
     * Then 권한 없음 오류 반환됨
     * When 로그인 없이 즐겨찾기 목록 조회 요청
     * Then 권한 없음 오류 반환됨
     * When 즐겨찾기 생성을 요청
     * And 로그인 없이 즐겨찾기 삭제 요청
     * Then 권한 없음 오류 반환됨
     */
    @DisplayName("로그인 없이 즐겨찾기를 관리한다.")
    @Test
    void deleteFavoriteWithoutAccessToken() {
        ExtractableResponse<Response> createResponse = 로그인_없이_즐겨찾기_생성_요청(sourceId, targetId);
        권한_없음_오류_반환됨(createResponse);

        ExtractableResponse<Response> showResponse = 로그인_없이_즐겨찾기_목록_조회_요청();
        권한_없음_오류_반환됨(showResponse);

        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, sourceId, targetId);
        ExtractableResponse<Response> deleteResponse = 로그인_없이_즐겨찾기_삭제_요청(response);
        권한_없음_오류_반환됨(deleteResponse);
    }
}
