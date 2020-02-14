package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.LineResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.time.LocalTime;
import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceTest extends AbstractAcceptanceTest {
    public static final String LINE_URL = "/lines";
    public static final String EDGE_URL = "/edges";
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;

    @BeforeEach
    void setUp() {
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.lineHttpTest = new LineHttpTest(webTestClient);
    }

    @Test
    public void createLine() {
        // when
        Long lineId = lineHttpTest.createLine(LINE_NAME);

        // then
        EntityExchangeResult<LineResponseView> getResponse = lineHttpTest.retrieveLine(lineId);
        assertThat(getResponse.getResponseBody().getName()).isEqualTo(LINE_NAME);
    }

    @Test
    public void retrieveLine() {
        // given
        Long lineId = lineHttpTest.createLine(LINE_NAME);

        // when
        EntityExchangeResult<LineResponseView> getResponse = lineHttpTest.retrieveLine(lineId);

        // then
        assertThat(getResponse.getResponseBody().getName()).isEqualTo(LINE_NAME);
        assertThat(getResponse.getResponseBody().getStartTime()).isEqualTo(LocalTime.of(0, 0).toString());
        assertThat(getResponse.getResponseBody().getEndTime()).isEqualTo(LocalTime.of(23, 30).toString());
        assertThat(getResponse.getResponseBody().getInterval()).isEqualTo(30);
    }

    @Test
    public void showLines() {
        // given
        lineHttpTest.createLineRequest(LINE_NAME);
        lineHttpTest.createLineRequest(LINE_NAME_2);
        lineHttpTest.createLineRequest(LINE_NAME_3);

        // when
        EntityExchangeResult<List<LineResponseView>> response = lineHttpTest.showLinesRequest();

        // then
        assertThat(response.getResponseBody().size()).isEqualTo(3);
    }

    @Test
    public void deleteLine() {
        // given
        Long lineId = lineHttpTest.createLine(LINE_NAME);

        // when
        webTestClient.delete().uri(LINE_URL + "/" + lineId)
                .exchange()
                .expectStatus().isNoContent();

        // then
        webTestClient.get().uri(LINE_URL + "/" + lineId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createEdge() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long lineId = lineHttpTest.createLine(LINE_NAME);

        // when
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);

        // then
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(LINE_URL + "/" + lineId);
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_2);
    }

    @Test
    public void deleteEdge() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long lineId = lineHttpTest.createLine(LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3);

        // when
        webTestClient.delete().uri(LINE_URL + "/" + lineId + EDGE_URL + "?stationId=" + stationId2)
                .exchange()
                .expectStatus().isOk();

        // then
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(LINE_URL + "/" + lineId);
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_3);

    }

    @Test
    public void retrieveLineWithStation() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long lineId = lineHttpTest.createLine(LINE_NAME);

        // when
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);

        // then
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(LINE_URL + "/" + lineId);
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_2);
    }
}
