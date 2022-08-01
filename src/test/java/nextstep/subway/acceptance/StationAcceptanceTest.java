package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceSteps.given;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 관리자가 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("관리자가 지하철역을 생성한다.")
    @Test
    void createStation_admin() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(관리자, "강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = given()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * When 관리자가 아닌 자가 지하철역을 생성하면
     * Then 지하철역이 생성되지 않는다.
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 없다.
     */
    @DisplayName("관리자가 아닌 자는 지하철을 생성할 수 없다.")
    @Test
    void createStation_member() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(사용자, "강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // then
        List<String> stationNames = given()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        지하철역_생성_요청(관리자, "강남역");
        지하철역_생성_요청(관리자, "역삼역");

        // when
        ExtractableResponse<Response> stationResponse = given()
                .when().get("/stations")
                .then().log().all()
                .extract();

        // then
        List<StationResponse> stations = stationResponse.jsonPath().getList(".", StationResponse.class);
        assertThat(stations).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 관리자가 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("관리자가 지하철역을 제거한다.")
    @Test
    void deleteStation_admin() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(관리자, "강남역");

        // when
        ExtractableResponse<Response> 삭제_요청_결과 = 지하철역_삭제_요청(관리자, createResponse);

        // then
        assertThat(삭제_요청_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<String> stationNames = given()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain("강남역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 관리자가 아닌자가 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("관리자가 아닌 자가 지하철역을 제거한다.")
    @Test
    void deleteStation_member() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(관리자, "강남역");

        // when
        ExtractableResponse<Response> 삭제_요청_결과 = 지하철역_삭제_요청(사용자, createResponse);

        // then
        assertThat(삭제_요청_결과.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        List<String> stationNames = given()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    private ExtractableResponse<Response> 지하철역_삭제_요청(String token, ExtractableResponse<Response> createResponse) {
        return given(token)
                .when()
                .delete(createResponse.header("location"))
                .then().log().all()
                .extract();
    }
}