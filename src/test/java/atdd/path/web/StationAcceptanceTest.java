package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.StationResponseView;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String STATION_URL = "/stations";

    private HttpTestUtils httpTestUtils;

    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;


    @BeforeEach
    void setUp() {
        httpTestUtils = new HttpTestUtils(webTestClient);

        this.stationHttpTest = new StationHttpTest(httpTestUtils);
        this.lineHttpTest = new LineHttpTest(httpTestUtils);

    }

    @DisplayName("지하철역 등록")
    @Test
    public void createStation() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        // when
        Long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);

        // then
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId, accessToken);
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("지하철역 정보 조회")
    @Test
    public void retrieveStation() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);

        // when
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId, accessToken);

        // then
        assertThat(response.getResponseBody().getId()).isNotNull();
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("구간이 연결된 지하철역 정보 조회")
    @Test
    public void retrieveStationWithLine() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2, accessToken);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3, accessToken);
        Long lineId = lineHttpTest.createLine(LINE_NAME, accessToken);
        Long lineId2 = lineHttpTest.createLine(LINE_NAME_2, accessToken);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, accessToken);
        lineHttpTest.createEdgeRequest(lineId2, stationId, stationId3, accessToken);

        // when
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId, accessToken);

        // then
        assertThat(response.getResponseBody().getId()).isEqualTo(stationId);
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
        assertThat(response.getResponseBody().getLines().size()).isEqualTo(2);
    }


    @DisplayName("지하철역 목록 조회")
    @Test
    public void showStations() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        stationHttpTest.createStationRequest(STATION_NAME, accessToken);
        stationHttpTest.createStationRequest(STATION_NAME_2, accessToken);
        stationHttpTest.createStationRequest(STATION_NAME_3, accessToken);

        // when
        EntityExchangeResult<List<StationResponseView>> response = stationHttpTest.showStationsRequest(accessToken);

        // then
        assertThat(response.getResponseBody().size()).isEqualTo(3);
    }

    @DisplayName("지하철역 삭제")
    @Test
    public void deleteStation() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId, accessToken);

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
