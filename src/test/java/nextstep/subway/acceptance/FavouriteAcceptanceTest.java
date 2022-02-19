package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능 인수테스트")
public class FavouriteAcceptanceTest extends AcceptanceTest {
    private static final String SOURCE = "source";
    private static final String TARGET = "target";

    private Long 사용자Id;
    private String 사용자토큰;
    private Long 강남역Id;
    private Long 역삼역Id;
    private Long 양재역Id;
    private Long 이호선Id;
    private Long 신분당선Id;

    /**
     * Background : 사용자와 역이 등록되어져있다.
     */
    @BeforeEach
    void init() {
        사용자Id = extractId(회원_생성_요청("test@email.com", "1234", 26));
        사용자토큰 = 토큰_인증("test@email.com", "1234");

        강남역Id = extractId(지하철역_생성_요청("강남역"));
        역삼역Id = extractId(지하철역_생성_요청("역삼역"));
        양재역Id = extractId(지하철역_생성_요청("양재역"));

        이호선Id = extractId(지하철_노선_생성_요청(createParams("이호선", "green", 강남역Id, 역삼역Id, 13)));
        신분당선Id = extractId(지하철_노선_생성_요청(createParams("신분당선", "orange", 강남역Id, 양재역Id, 10)));
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private Map<String, String> createParams(String lineName, String lineColor, Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");

        return params;
    }

    /**
     * Scenario : 인증된 사용자가 즐겨찾기 기능을 사용한다.
     *
     * <즐겨찾기 등록>
     * When     : 인증된 사용자가 두 역에 대해 즐겨찾기를 요청하면,
     * Then     : 선호 경로가 등록된다.
     *
     * <즐겨찾기 조회>
     * When     : 인증된 사용자가 즐겨찾기 목록을 조회하면,
     * Then     : 즐겨찾기된 선호경로들이 조회된다.
     *
     * <즐겨찾기 삭제>
     * When     : 인증된 사용자가 등록된 선호경로에 대해 즐겨찾기 취소를 요청하면,
     * Then     : 해당 선호경로에 대한 즐겨찾기가 취소된다.
     */
    @Test
    @DisplayName("인증된 회원이 즐겨찾기 기능을 수행한다.")
    void 즐겨찾기_기능() {
        Map<String, Long> requestBody = new HashMap<>(2);
        requestBody.put(SOURCE, 강남역Id);
        requestBody.put(TARGET, 역삼역Id);

        /* 즐겨찾기 등록 */
        // when
        ExtractableResponse<Response> postResponse = RestAssured
                .given().log().all()
                .auth().preemptive().oauth2(사용자토큰)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)

                .when()
                .post("/favourites")

                .then().log().all()
                .extract();

        // then
        assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(postResponse.header("Location")).isEqualTo("/favourites/1");

        /* 즐겨찾기 조회 */
        // when
        ExtractableResponse<Response> getResponse = RestAssured
                .given().log().all()
                .auth().preemptive().oauth2(사용자토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/favourites")

                .then().log().all()
                .extract();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.body().jsonPath().getList("..name")).containsExactly(Arrays.array("강남역", "역삼역"));

        /* 즐겨찾기 취소 */
        // when
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().log().all()
                .auth().preemptive().oauth2(사용자토큰)

                .when()
                .delete("/favourites/1")

                .then().log().all()
                .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Scenario : 인증된 사용자가 즐겨찾기를 요청하면 선호 경로가 등록된다.
     * When     : 인증된 사용자가 즐겨찾기를 요청하면
     * Then     : 선호 경로가 등록된다.
     */
    @Test
    @DisplayName("인증된 회원이 즐겨찾기를 한다.")
    void 즐겨찾기_수행() {
        Map<String, Long> requestBody = new HashMap<>(2);
        requestBody.put(SOURCE, 강남역Id);
        requestBody.put(TARGET, 역삼역Id);


    }

    @Test
    @DisplayName("인증된 회원이 즐겨찾기한 선호 경로를 취소한다.")
    void 즐겨찾기_취소() {

    }

    @Test
    @DisplayName("내 선호 경로 리스트를 조회한다.")
    void 즐겨찾기_리스트_조회() {

    }

    @Test
    @DisplayName("인증되지 않은 회원이 즐겨찾기를 접근하면 에러를 반환한다.")
    void 미인증_사용자_즐겨찾기_접근() {

    }

    @Test
    @DisplayName("즐겨찾기가 되지 않은 선호 경로에 대해 취소요청을 하면 에러를 반환한다.")
    void 즐겨찾기_되지않은_경로를_취소() {

    }

    @Test
    @DisplayName("없는 선호 경로에 대해 취소요청을 하면 에러를 반환한다.")
    void 없는_선호경로에_대한_취소() {

    }

    @Test
    @DisplayName("없는 역을 대상으로 즐겨찾기를 시도할 경우 에러를 반환한다.")
    void 없는_역에_대한_취소() {

    }
}
