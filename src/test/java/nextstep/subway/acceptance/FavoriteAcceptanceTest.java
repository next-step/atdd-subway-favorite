package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.acceptance.model.MemberEntitiesHelper.로그인_되어_있음;
import static nextstep.auth.acceptance.model.MemberEntitiesHelper.회원가입을_한다;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_목록_조회됨;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_삭제됨;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.model.FavoriteEntitiesHelper.즐겨찾기_생성됨;
import static nextstep.subway.acceptance.model.LineEntitiesHelper.newLine;
import static nextstep.subway.acceptance.model.StationEntitiesHelper.강남역;
import static nextstep.subway.acceptance.model.StationEntitiesHelper.역삼역;
import static nextstep.subway.acceptance.model.StationEntitiesHelper.지하철역_생성_요청후_아이디_조회;
import static org.apache.http.HttpHeaders.LOCATION;

class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 역삼역_ID;
    private Long 강남역_ID;
    private Map<String, Object> 이호선 = new HashMap<>();
    private String accessToken;

    @BeforeEach
    void setFixtures() {
        역삼역_ID = 지하철역_생성_요청후_아이디_조회(역삼역);
        강남역_ID = 지하철역_생성_요청후_아이디_조회(강남역);
        이호선 = newLine("이호선", "bg-green-600", 강남역_ID, 역삼역_ID, 2);
        회원가입을_한다();
        accessToken = 로그인_되어_있음();
    }

    @Test
    void 즐겨찾기를_관리() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역_ID, 역삼역_ID);
        즐겨찾기_생성됨(createResponse);
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);
        즐겨찾기_목록_조회됨(findResponse);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse.header(LOCATION));
        즐겨찾기_삭제됨(deleteResponse);
    }
}
