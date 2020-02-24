package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static atdd.path.TestConstant.*;

public class GraphAcceptanceTest extends AbstractAcceptanceTest {
    private HttpTestUtils httpTestUtils;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;

    @BeforeEach
    void setUp() {
        this.httpTestUtils = new HttpTestUtils(webTestClient);
        this.stationHttpTest = new StationHttpTest(httpTestUtils);
        this.lineHttpTest = new LineHttpTest(httpTestUtils);
    }

    @Test
    public void findPath() {
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

        webTestClient.get().uri("/paths?startId=" + stationId + "&endId=" + stationId4)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.startStationId").isEqualTo(stationId)
                .jsonPath("$.endStationId").isEqualTo(stationId4)
                .jsonPath("$.stations.length()").isEqualTo(4);
    }

}
