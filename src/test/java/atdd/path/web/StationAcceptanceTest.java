package atdd.path.web;

import atdd.AbstractAcceptanceTest;
import atdd.path.application.dto.StationResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String STATION_URL = "/stations";

    private LineHttpTest lineHttpTest;
    private StationHttpTest stationHttpTest;

    @BeforeEach
    void setUp() {
        this.lineHttpTest = new LineHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
    }

    @DisplayName("지하철역 등록")
    @Test
    public void createStation() {
        // when
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        // then
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId);
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("지하철역 정보 조회")
    @Test
    public void retrieveStation() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        // when
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId);

        // then
        assertThat(response.getResponseBody().getId()).isNotNull();
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("구간이 연결된 지하철역 정보 조회")
    @Test
    public void retrieveStationWithLine() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long lineId = lineHttpTest.createLine(LINE_NAME);
        Long lineId2 = lineHttpTest.createLine(LINE_NAME_2);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        lineHttpTest.createEdgeRequest(lineId2, stationId, stationId3);

        // when
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId);

        // then
        assertThat(response.getResponseBody().getId()).isEqualTo(stationId);
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
        assertThat(response.getResponseBody().getLines().size()).isEqualTo(2);
    }


    @DisplayName("지하철역 목록 조회")
    @Test
    public void showStations() {
        // given
        stationHttpTest.createStationRequest(STATION_NAME);
        stationHttpTest.createStationRequest(STATION_NAME_2);
        stationHttpTest.createStationRequest(STATION_NAME_3);

        // when
        EntityExchangeResult<List<StationResponseView>> response = stationHttpTest.showStationsRequest();

        // then
        assertThat(response.getResponseBody().size()).isEqualTo(3);
    }

    @DisplayName("지하철역 삭제")
    @Test
    public void deleteStation() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId);

        // when
        webTestClient.delete().uri(STATION_URL + "/" + stationId)
                .exchange()
                .expectStatus().isNoContent();

        // then
        webTestClient.get().uri(STATION_URL + "/" + stationId)
                .exchange()
                .expectStatus().isNotFound();
    }
}
