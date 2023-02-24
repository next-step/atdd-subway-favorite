package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청하고_토큰_반환;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private Long 신분당선;

    private Long 강남역;
    private Long 양재역;

    private String token;

    /**
     * given: 지하철 노선 및 역을 추가하고
     * given: 회원 가입을하고(super.setUp)
     * given: 로그인을 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
        신분당선 = 지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");

        token = 베어러_인증_로그인_요청하고_토큰_반환(EMAIL, PASSWORD);
    }

    /**
     * given: 올바른 로그인 토큰으로
     * when : 경로 즐겨찾기 추가 요청을 하면
     * then : 즐겨찾기 등록을 성공하고
     * then : 즐겨찾기 목록 조회시 추가한 경로를 확인할 수 있다.
     */
    @DisplayName("올바른 인증 토큰으로 경로 즐겨찾기 추가 요청을 하면 즐겨찾기 목록에서 해당 구간을 확인할 수 있다.")
    @Test
    void addFavorite() {
        RestAssured
                .given()
                    .auth().oauth2(token)
                    .accept(MediaType.ALL_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(Map.of("source", 강남역, "target", 양재역))
                .when()
                    .post("/favorites")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value())
                .extract();


        final List<FavoriteResponse> favorites = RestAssured
                .given()
                    .auth().oauth2(token)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/favorites")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("", FavoriteResponse.class);

        assertThat(favorites.size()).isEqualTo(1);
        final FavoriteResponse favoriteResponse = favorites.get(0);
        assertThat(favoriteResponse.getSource().getName()).isEqualTo("강남역");
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo("강남역");
    }


    private Map<String, String> createLineCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "신분당선");
        lineCreateParams.put("color", "bg-red-600");
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", 10 + "");
        return lineCreateParams;
    }
}
