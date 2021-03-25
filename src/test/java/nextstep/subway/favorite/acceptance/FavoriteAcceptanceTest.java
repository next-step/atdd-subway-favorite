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
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static int AGE = 30;

    private LineResponse 이호선;
    private StationResponse 신림역;
    private StationResponse 봉천역;
    private StationResponse 사당역;
    private StationResponse 서초역;
    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();
        신림역 = 지하철역_등록되어_있음("신림역").as(StationResponse.class);
        봉천역 = 지하철역_등록되어_있음("봉천역").as(StationResponse.class);
        사당역 = 지하철역_등록되어_있음("사당역").as(StationResponse.class);
        서초역 = 지하철역_등록되어_있음("서초역").as(StationResponse.class);

        Map<String, String> lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "이호선");
        lineCreateParams.put("color", "bg-green-300");
        lineCreateParams.put("upStationId", 신림역.getId() + "");
        lineCreateParams.put("downStationId", 봉천역.getId() + "");
        lineCreateParams.put("distance", 3 + "");
        이호선 = 지하철_노선_등록되어_있음(lineCreateParams).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(이호선, 봉천역, 사당역, 5);
        지하철_노선에_지하철역_등록_요청(이호선, 사당역, 서초역, 6);

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void favoriteAdministrate() {
        ExtractableResponse<Response> create = 즐겨찾기_생성_요청(tokenResponse, 신림역.getId(), 서초역.getId());;
        즐겨찾기_요청_응답_확인(create, HttpStatus.CREATED);

        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(tokenResponse);
        즐겨찾기_요청_응답_확인(response, HttpStatus.OK);

        ExtractableResponse<Response> delete = 즐겨찾기_삭제_요청(tokenResponse, create);
        즐겨찾기_요청_응답_확인(delete, HttpStatus.NO_CONTENT);
    }
}
