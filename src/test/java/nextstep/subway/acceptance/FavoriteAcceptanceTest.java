package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_되어_있음;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역id;
    private Long 양재역id;

    /**
     * Background
     * Given 지하철 역이 등록되어 있다.
     * And 회원 등록되어 있다.
     * And 로그인이 되어 있다.
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역id = StationSteps.지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역id = StationSteps.지하철역_생성_요청("양재역").jsonPath().getLong("id");
    }

    /**
     * Scenario: 즐겨찾기 기능을 관리한다.
     * When 즐겨찾기 생성 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기 기능을 관리한다.")
    @Test
    void manageFavorite() {
        // given
        회원_생성_되어_있음(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역id, 양재역id);

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        즐겨찾기_목록_조회됨(response, 강남역id, 양재역id);

        // when
        ExtractableResponse<Response> deletedResponse = 즐겨찾기_삭제_요청(accessToken, createResponse);

        즐겨찾기_삭제됨(deletedResponse);
    }
}
