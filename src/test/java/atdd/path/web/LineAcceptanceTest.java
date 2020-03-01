package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.LineResponseView;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.time.LocalTime;
import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceTest extends AbstractAcceptanceTest {
    public static final String LINE_URL = "/lines";
    public static final String EDGE_URL = "/edges";

    private HttpTestUtils httpTestUtils;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;

    @BeforeEach
    void setUp() {
        this.httpTestUtils = new HttpTestUtils(webTestClient);

        this.stationHttpTest = new StationHttpTest(httpTestUtils);
        this.lineHttpTest = new LineHttpTest(httpTestUtils);
    }

    @DisplayName("지하철 노선 등록")
    @Test
    public void createLine() {
        // when
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long lineId = lineHttpTest.createLine(LINE_NAME, accessToken);

        // then
        EntityExchangeResult<LineResponseView> getResponse = lineHttpTest.retrieveLine(lineId, accessToken);
        assertThat(getResponse.getResponseBody().getName()).isEqualTo(LINE_NAME);
    }

    @DisplayName("지하철 노선 정보 조회")
    @Test
    public void retrieveLine() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long lineId = lineHttpTest.createLine(LINE_NAME, accessToken);

        // when
        EntityExchangeResult<LineResponseView> getResponse = lineHttpTest.retrieveLine(lineId, accessToken);

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
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2, accessToken);
        Long lineId = lineHttpTest.createLine(LINE_NAME, accessToken);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, accessToken);

        // when
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(lineId, accessToken);

        // then
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_2);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    public void showLines() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        lineHttpTest.createLineRequest(LINE_NAME, accessToken);
        lineHttpTest.createLineRequest(LINE_NAME_2, accessToken);
        lineHttpTest.createLineRequest(LINE_NAME_3, accessToken);

        // when
        EntityExchangeResult<List<LineResponseView>> response = lineHttpTest.showLinesRequest(accessToken);

        // then
        assertThat(response.getResponseBody().size()).isEqualTo(3);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    public void deleteLine() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long lineId = lineHttpTest.createLine(LINE_NAME, accessToken);

        // when
        lineHttpTest.deleteById(lineId, accessToken);

        // then
        httpTestUtils.getRequestNotFound(LINE_URL + "/" + lineId, accessToken);
    }

    @DisplayName("지하철노선에 지하철 구간을 등록")
    @Test
    public void createEdge() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2, accessToken);
        Long lineId = lineHttpTest.createLine(LINE_NAME, accessToken);

        // when
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, accessToken);

        // then
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(lineId, accessToken);
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_2);
    }

    @DisplayName("지하철노선에 지하철 구간을 제외")
    @Test
    public void deleteEdge() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        Long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2, accessToken);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3, accessToken);
        Long lineId = lineHttpTest.createLine(LINE_NAME, accessToken);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, accessToken);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3, accessToken);

        // when
        webTestClient.delete().uri(LINE_URL + "/" + lineId + EDGE_URL + "?stationId=" + stationId2)
                .exchange()
                .expectStatus().isOk();

        // then
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(lineId, accessToken);
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_3);
    }
}
