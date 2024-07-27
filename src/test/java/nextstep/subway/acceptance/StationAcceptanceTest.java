package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다")
    @Test
    void testCreateStation() {
        // when
        ExtractableResponse<Response> response = createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
            RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("이름이 없는 지하철역을 생성 시도 한다")
    @Test
    void testCreateStationWithEmptyName() {
        // when
        ExtractableResponse<Response> response = createStation(null);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다")
    @Test
    void testGetStations() {
        // given
        String stationNameGangnam = "강남역";
        String stationNameYeoksam = "역삼역";

        createStation(stationNameGangnam);
        createStation(stationNameYeoksam);

        // when
        ExtractableResponse<Response> response = getAllStations();

        // then
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsExactlyInAnyOrder(stationNameGangnam, stationNameYeoksam);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다")
    @Test
    void testDeleteStation() {
        // given
        ExtractableResponse<Response> response = createStation("강남역");

        String location = response.header("Location");
        Long stationId = Long.parseLong(location.split("/stations/")[1]);

        // when
        deleteStation(stationId);

        // then
        ExtractableResponse<Response> allStations = getAllStations();
        List<String> stationNames = allStations.jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain("강남역");
    }

    @DisplayName("존재하지 않는 지하철역을 삭제 시도한다")
    @Test
    void testDeleteNonExistingStation() {
        // when
        ExtractableResponse<Response> response = deleteStation(999L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("이미 삭제된 지하철역을 다시 삭제 시도한다")
    @Test
    void testDeleteAlreadyDeletedStation() {
        // given
        ExtractableResponse<Response> response = createStation("강남역");

        String location = response.header("Location");
        Long stationId = Long.parseLong(location.split("/stations/")[1]);

        deleteStation(stationId);

        // when
        ExtractableResponse<Response> secondDeleteResponse = deleteStation(stationId);

        // then
        assertThat(secondDeleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}