package atdd.path.web;

import atdd.AbstractAcceptanceTest;
import atdd.path.application.dto.LineResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.time.LocalTime;
import java.util.List;

import static atdd.TestConstant.*;
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

    @DisplayName("지하철 노선 등록")
    @Test
    public void createLine() {
        // when
        Long lineId = lineHttpTest.createLine(LINE_NAME);

        // then
        EntityExchangeResult<LineResponseView> getResponse = lineHttpTest.retrieveLine(lineId);
        assertThat(getResponse.getResponseBody().getName()).isEqualTo(LINE_NAME);
    }

    @DisplayName("지하철 노선 정보 조회")
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

    @DisplayName("구건이 연결된 지하철 노선 조회")
    @Test
    public void retrieveLineWithStation() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long lineId = lineHttpTest.createLine(LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);

        // when
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(LINE_URL + "/" + lineId);

        // then
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_2);
    }

    @DisplayName("지하철 노선 목록 조회")
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

    @DisplayName("지하철 노선 삭제")
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

    @DisplayName("지하철노선에 지하철 구간을 등록")
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

    @DisplayName("지하철노선에 지하철 구간을 제외")
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
}
