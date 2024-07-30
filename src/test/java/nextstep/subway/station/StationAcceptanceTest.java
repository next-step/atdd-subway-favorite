package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

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
        ExtractableResponse<Response> response = StationAssuredTemplate.createStation("강남역")
                .then().log().all()
                .extract();

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

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 역 목록을 조회하면 모든 지하철 역 정보를 응답받습니다.")
    @Test
    void showStations() {
        // given
        String firstStationName = "station1";
        String secondStationName = "station2";

        StationAssuredTemplate.createStationWithId(firstStationName);
        StationAssuredTemplate.createStationWithId(secondStationName);

        // when
        List<String> stationList = StationAssuredTemplate.showStations()
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        // then
        assertThat(stationList).hasSize(2);
        assertThat(stationList).containsExactly(firstStationName, secondStationName);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("특정 지하철 역을 삭제하면 해당 지하철역은 조회되지 않습니다.")
    @Test
    void deleteStations() {
        // given
        String firstStationName = "station1";
        String secondStationName = "station2";

        long stationId = StationAssuredTemplate.createStationWithId(firstStationName);

        StationAssuredTemplate.createStationWithId(secondStationName);
        StationAssuredTemplate.deleteStation(stationId);

        // when
        List<String> stationList = StationAssuredTemplate.showStations()
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        // then
        Assertions.assertThat(stationList).hasSize(1);
        Assertions.assertThat(stationList).containsExactly(secondStationName);
    }
}