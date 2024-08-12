package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends BasicAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var response = StationCommonApi.createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = StationCommonApi.findAllStations()
            .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStationNames() {
        //given
        StationCommonApi.createStation("부평구청역");
        StationCommonApi.createStation("가산디지털단지역");

        // when
        List<String> stationNames = StationCommonApi.findAllStations()
            .jsonPath().getList("name", String.class);

        //then
        assertThat(stationNames).containsExactly("부평구청역", "가산디지털단지역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        //given
        var response = StationCommonApi.createStation("강남역");

        //when
        StationCommonApi.deleteStation(response.jsonPath().getLong("id"));

        //then
        List<String> stationNames = StationCommonApi.findAllStations().jsonPath()
            .getList("name", String.class);
        assertThat(stationNames).doesNotContain("강남역");
    }
}