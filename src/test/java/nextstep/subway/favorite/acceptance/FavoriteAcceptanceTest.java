package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.favorite.steps.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_등록되어_있음;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관리 인수테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    private static StationResponse 강남역, 역삼역, 선릉역;
    private LineResponse 이호선;
    private TokenResponse tokenResponse;
    private Map<String, String> params = new HashMap<>();
    private FavoriteRequest favoriteRequest;

    @BeforeEach
    public void setUp(){
        super.setUp();
        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(new LineRequest("2호선", "green", 강남역.getId(), 역삼역.getId(), 10)).as(LineResponse.class);
        지하철_노선에_지하철역_등록됨(이호선, new SectionRequest(역삼역.getId(), 선릉역.getId(), 5));

        회원_등록되어_있음(new MemberRequest(EMAIL, PASSWORD, AGE)); // Location : memberId
        tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        favoriteRequest = new FavoriteRequest(강남역.getId(), 선릉역.getId());
    }

    @DisplayName("즐겨찾기를 생성/조회/삭제")
    @Test
    void manageFavorite(){

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(tokenResponse, favoriteRequest);

        // then
        Long createdId = 즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(tokenResponse);

        // then
        즐겨찾기_목록_조회됨(response);
        즐겨찾기_목록_포함됨(response, createdId);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청됨(tokenResponse, createdId);

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("[오류케이스] 즐겨찾기를 생성/조회/삭제 - 권한이 없는 경우")
    @Test
    void manageFavoriteWithUnauthorized(){

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(new TokenResponse("0000"), favoriteRequest);

        // then
        권한없음(createResponse);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(new TokenResponse("0000"));

        // then
        권한없음(response);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청됨(new TokenResponse("0000"), 1L);

        // then
        권한없음(deleteResponse);
    }

}
