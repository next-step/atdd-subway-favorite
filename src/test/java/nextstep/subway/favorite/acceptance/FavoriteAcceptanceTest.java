package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.station.StationSteps.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // background
        지하철역_등록되어_있음(강남역);
        지하철역_등록되어_있음(역삼역);
        // 지하철 노선 등록, 지하철 노선에 지하철역 등록, 회원 등록, 로그인
   }

    @DisplayName("즐겨찾기 통합 시나리오")
    @Test
    void favoriteScenario() {
        ExtractableResponse<Response> firstCreateResponse = 즐겨찾기_생성_요청();
        즐겨찾기_생성됨(firstCreateResponse);

        ExtractableResponse<Response> secondCreateResponse = 즐겨찾기_생성_요청();
        즐겨찾기_생성됨(secondCreateResponse);

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        즐겨찾기_목록_조회됨(getResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청();
        즐겨찾기_삭제됨(deleteResponse);

    }
}
