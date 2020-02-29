package atdd.path.web;

import atdd.path.application.dto.edge.CreateEdgeRequestView;
import atdd.path.application.dto.line.LineResponseView;
import atdd.path.application.dto.station.StationResponseView;
import atdd.path.domain.User;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static atdd.path.web.LineAcceptanceTest.LINE_INPUT_JSON;
import static atdd.path.web.LineAcceptanceTest.LINE_URL;
import static atdd.path.web.StationAcceptanceTest.STATION_URL;
import static atdd.path.web.UserAcceptanceTest.KIM_INPUT_JSON;
import static atdd.path.web.UserAcceptanceTest.USER_BASE_URL;

public class CreateWebClientTest extends RestWebClientTest {

    public CreateWebClientTest(WebTestClient webTestClient) {
        super(webTestClient);
    }

    String createUser() {
        return Objects.requireNonNull(
                postMethodAcceptance(USER_BASE_URL + "/sigh-up", KIM_INPUT_JSON, User.class)
                .getResponseHeaders()
                .getLocation()
                .getPath());
    }

    Long createStation(String stationName, String jwt) {
        StationResponseView responseView = new StationResponseView(1L, stationName);
        return Objects.requireNonNull(
                postMethodWithAuthAcceptance(STATION_URL, responseView, StationResponseView.class, jwt)
                        .getResponseBody().getId());
    }

    Long createLine(String jwt) {
        return Objects.requireNonNull(
                postMethodWithAuthAcceptance(LINE_URL, LINE_INPUT_JSON, LineResponseView.class, jwt)
                        .getResponseBody().getId());
    }

    void createEdge(Long lineId, Long sourceStationId, Long targetStationId, int distance, String jwt) {
        CreateEdgeRequestView requestView = new CreateEdgeRequestView(sourceStationId, targetStationId, distance);
        Objects.requireNonNull(
                postMethodWithAuthAcceptance(LINE_URL + "/" + lineId + "/edges", requestView, Void.class, jwt))
                .getResponseBody();
    }
}
