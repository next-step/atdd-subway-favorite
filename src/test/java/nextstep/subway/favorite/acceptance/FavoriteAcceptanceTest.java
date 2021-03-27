package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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

        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 강남역, 역삼역, 10);

        회원_생성_요청(EMAIL, PASSWORD, 20);
   }

    @DisplayName("즐겨찾기 통합 시나리오")
    @Test
    void favoriteScenario() {
        // given
        로그인_되어_있음(EMAIL, PASSWORD);

        // when, then
        ExtractableResponse<Response> firstCreateResponse = 즐겨찾기_생성_요청();
        즐겨찾기_생성됨(firstCreateResponse);

        ExtractableResponse<Response> secondCreateResponse = 즐겨찾기_생성_요청();
        즐겨찾기_생성됨(secondCreateResponse);

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        즐겨찾기_목록_조회됨(getResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청();
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("로그인되어있지 않으면 조회 불가")
    @Test
    void favoriteUnauthorized() {
        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        즐겨찾기_목록_조회_실패됨(getResponse);
    }

    @DisplayName("다른 유저로 로그인되어있으면 조회 불가")
    @Test
    void name() {
        // given
        회원_생성_요청("another@gmail.com", "1234", 22);
        로그인_되어_있음("another@gmail.com", "1234");

        // when, then
        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        즐겨찾기_목록_조회_실패됨(getResponse);
    }
}
