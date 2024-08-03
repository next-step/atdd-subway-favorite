package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestUtil.상태코드_CREATED;
import static nextstep.subway.acceptance.AcceptanceTestUtil.역_생성_후_id_추출;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = createStation("강남역");

        // then
        상태코드_CREATED(response);

        // then
        List<String> stationNames = requestSpecificationWithLog()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void viewStations() {
        // given
        createStation("서울역");
        createStation("부산역");

        // when
        List<String> stationNames = requestSpecificationWithLog()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        // then
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactlyInAnyOrder("서울역", "부산역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        long stationId = 역_생성_후_id_추출("사당역");

        // when
        requestSpecificationWithLog()
                .when().delete("/stations/" + stationId)
                .then()
                .extract();

        // then
        List<String> stationNames = requestSpecificationWithLog()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).doesNotContain("사당역");
    }

    private ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return requestSpecificationWithLog()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private RequestSpecification requestSpecificationWithLog() {
        return RestAssured.given().log().all();
    }
}