package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceSteps.given;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 남부터미널역;
    private Long 양재역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
    }

    /**
     * when: 즐겨찾기 생성 요청을 하면
     * then: 즐겨찾기가 추가되고
     * then: 해당 즐겨찾기 정보를 조회할 수 있다.
     */
    @Test
    @DisplayName("즐겨찾기 생성")
    void createFavorite() {
        // when
        var 생성_요청_결과 = 즐겨찾기_생성_요청(사용자, 교대역, 양재역);

        // then
        즐겨찾기_생성됨(생성_요청_결과);
        var 즐겨찾기_조회_정보 = 즐겨찾기_조회_요청(사용자, 생성_요청_결과);
        즐겨찾기_조회됨(즐겨찾기_조회_정보, 교대역, 양재역);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return given(accessToken)
                .body(params)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return given(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response, Long source, Long target) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("source.id", Long.class)).containsOnly(source, target);
    }
}
