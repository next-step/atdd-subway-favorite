package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.PathResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GraphAcceptanceTest extends AbstractAcceptanceTest {

    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;
    private GraphHttpTest graphHttpTest;

    @BeforeEach
    void setUp() {
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.lineHttpTest = new LineHttpTest(webTestClient);
        this.graphHttpTest = new GraphHttpTest(webTestClient);
    }

    @Test
    public void findPath() {
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long stationId4 = stationHttpTest.createStation(STATION_NAME_4);
        Long lineId = lineHttpTest.createLine(LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3);
        lineHttpTest.createEdgeRequest(lineId, stationId3, stationId4);

        PathResponseView view = graphHttpTest.findPath(stationId, stationId4);

        assertThat(view).isNotNull();
        assertThat(view.getStartStationId()).isEqualTo(STATION_ID);
        assertThat(view.getEndStationId()).isEqualTo(STATION_ID_4);
        assertThat(view.getStations().size()).isEqualTo(4);
    }

}
