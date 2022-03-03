package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_등록_요청;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_등록됨;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_삭제됨;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.FavoritesSteps.즐겨찾기_조회됨;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관리 기능")
public class FavoritesAcceptanceTest extends AcceptanceTest {

    /**
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */

    @Test
    @DisplayName("즐겨찾기 관리")
    void favorite() {
        Long 강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        Long 양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        Long 이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(강남역, 양재역));

        회원_생성_요청("hi@email.com", "password", 10);
        String accessToken = 로그인_되어_있음("hi@email.com", "password");

        ExtractableResponse<Response> createResponse = 즐겨찾기_등록_요청(accessToken, new FavoriteRequest(강남역, 양재역));
        즐겨찾기_등록됨(createResponse);

        ExtractableResponse<Response> findResponse = 즐겨찾기_조회_요청(accessToken);
        즐겨찾기_조회됨(findResponse, 강남역, 양재역);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse.header("Location"));
        즐겨찾기_삭제됨(deleteResponse);
    }

    @Test
    @DisplayName("로그인 하지 않은 상태에서 권한이 필요한 요청시 401 응답한다")
    void unauthorized() {
        Long 강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        Long 양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        ExtractableResponse<Response> response = 즐겨찾기_등록_요청("123", new FavoriteRequest(강남역, 양재역));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", 6 + "");
        return params;
    }
}
