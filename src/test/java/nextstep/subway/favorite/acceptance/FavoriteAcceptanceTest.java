package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.station.StationSteps.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;

    private StationResponse 시청역;
    private StationResponse 이대역;

    private LineResponse 이호선;

    private String EMAIL = "member@gmail.com";
    private String PASSWORD = "1234";
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // background
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);

        시청역 = 지하철역_등록되어_있음("시청역").as(StationResponse.class);
        이대역 = 지하철역_등록되어_있음("이대역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 강남역, 역삼역, 10);

        회원_생성_요청(EMAIL, PASSWORD, 20);
   }

    @DisplayName("즐겨찾기 통합 시나리오")
    @Test
    void favoriteScenario() {
        // given
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when, then
        ExtractableResponse<Response> firstCreateResponse = 즐겨찾기_생성_요청(tokenResponse, new FavoriteRequest(강남역.getId(), 역삼역.getId()));
        즐겨찾기_생성됨(firstCreateResponse);

        ExtractableResponse<Response> secondCreateResponse = 즐겨찾기_생성_요청(tokenResponse, new FavoriteRequest(시청역.getId(), 이대역.getId()));        즐겨찾기_생성됨(secondCreateResponse);
        즐겨찾기_생성됨(secondCreateResponse);

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청(tokenResponse);
        즐겨찾기_목록_조회됨(getResponse, 2);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(tokenResponse, firstCreateResponse);
        즐겨찾기_삭제됨(deleteResponse);

        ExtractableResponse<Response> getResponseAfterDelete = 즐겨찾기_목록_조회_요청(tokenResponse);
        즐겨찾기_목록_조회됨(getResponseAfterDelete, 1);

    }

    @DisplayName("유효하지 않은 로그인 정보로는 조회 불가")
    @Test
    void getFavoritesWithoutLogin() {
        // given
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> firstCreateResponse = 즐겨찾기_생성_요청(tokenResponse, new FavoriteRequest(강남역.getId(), 역삼역.getId()));
        즐겨찾기_생성됨(firstCreateResponse);

        // when, then
        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청(new TokenResponse(""));
        즐겨찾기_목록_조회_권한없음(getResponse);
    }

    @DisplayName("다른 유저의 즐겨찾기는 삭제 불가")
    @Test
    void favoriteUnauthorizedDelete() {
        // given
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> firstCreateResponse = 즐겨찾기_생성_요청(tokenResponse, new FavoriteRequest(강남역.getId(), 역삼역.getId()));
        즐겨찾기_생성됨(firstCreateResponse);

        // when, then
        회원_생성_요청("another@gmail.com", "1234", 22);
        TokenResponse anotherTokenResponse = 로그인_되어_있음("another@gmail.com", "1234");

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(anotherTokenResponse, firstCreateResponse);
        즐겨찾기_삭제_권한없음(deleteResponse);

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청(tokenResponse);
        즐겨찾기_목록_조회됨(getResponse, 1);
    }
}
