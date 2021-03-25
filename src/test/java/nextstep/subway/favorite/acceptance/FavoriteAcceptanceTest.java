package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberSteps;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private TokenResponse 정상토큰;
    private TokenResponse 비정상토큰;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static int AGE = 10;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        Map<String, String> lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "신분당선");
        lineCreateParams.put("color", "bg-red-600");
        lineCreateParams.put("upStationId", 강남역.getId() + "");
        lineCreateParams.put("downStationId", 양재역.getId() + "");
        lineCreateParams.put("distance", 10 + "");
        신분당선 = 지하철_노선_등록되어_있음(lineCreateParams).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 5);
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 6);

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        정상토큰 = MemberSteps.로그인_되어_있음(EMAIL, PASSWORD);
        비정상토큰 = new TokenResponse("i1am2not3valid4token");
    }

    @DisplayName("Scenario : 즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // given
        Map<String, String> params = 즐겨찾기_파라미터_설정(강남역.getId() + "", 양재역.getId() + "");

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(params, 정상토큰);

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> invalidCreateResponse = 즐겨찾기_생성_요청(params, 비정상토큰);

        // then
        인증_실패함(invalidCreateResponse);

        // when
        ExtractableResponse<Response> readAllResponse = 즐겨찾기_목록_조회_요청(정상토큰);

        // then
        즐겨찾기_목록_조회됨(readAllResponse);

        // when
        ExtractableResponse<Response> invalidReadAllResponse = 즐겨찾기_목록_조회_요청(비정상토큰);

        // then
        인증_실패함(invalidReadAllResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse, 정상토큰);

        // then
        즐겨찾기_삭제됨(deleteResponse);

        // when
        ExtractableResponse<Response> invalidDeleteResponse = 즐겨찾기_삭제_요청(createResponse, 비정상토큰);

        // then
        인증_실패함(invalidDeleteResponse);
    }
}
