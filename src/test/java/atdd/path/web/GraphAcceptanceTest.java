package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.PathResponseView;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GraphAcceptanceTest extends AbstractAcceptanceTest {
    private HttpTestUtils httpTestUtils;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;
    private GraphHttpTest graphHttpTest;

    @BeforeEach
    void setUp() {
        this.httpTestUtils = new HttpTestUtils(webTestClient);
        this.stationHttpTest = new StationHttpTest(httpTestUtils);
        this.lineHttpTest = new LineHttpTest(httpTestUtils);
        this.graphHttpTest = new GraphHttpTest(httpTestUtils);
    }

    @Test
    public void findPath() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2, accessToken);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3, accessToken);
        Long stationId4 = stationHttpTest.createStation(STATION_NAME_4, accessToken);
        Long lineId = lineHttpTest.createLine(LINE_NAME, accessToken);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, accessToken);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3, accessToken);
        lineHttpTest.createEdgeRequest(lineId, stationId3, stationId4, accessToken);

        //when
        PathResponseView pathResponseView = graphHttpTest.findPath(stationId, stationId4, accessToken).getResponseBody();

        //then
        assertThat(pathResponseView.getStartStationId()).isEqualTo(stationId);
        assertThat(pathResponseView.getEndStationId()).isEqualTo(stationId4);
        assertThat(pathResponseView.getStations().size()).isEqualTo(4);
    }
}
