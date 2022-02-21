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
}
