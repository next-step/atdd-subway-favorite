package atdd.path.web;

import atdd.path.application.dto.line.LineResponseView;
import atdd.path.application.dto.station.StationResponseView;
import atdd.path.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static atdd.path.web.LineAcceptanceTest.LINE_INPUT_JSON;
import static atdd.path.web.LineAcceptanceTest.LINE_URL;
import static atdd.path.web.StationAcceptanceTest.STATION_URL;
import static atdd.path.web.UserAcceptanceTest.KIM_INPUT_JSON;
import static atdd.path.web.UserAcceptanceTest.USER_BASE_URL;

public class RestWebClientTest {
    private static final String NO_AUTHORIZATION = "";
    private WebTestClient webTestClient;

    public RestWebClientTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    <T> EntityExchangeResult<T> postMethodAcceptance(String uri, Object requestBody, Class<T> bodyClass) {
        return postMethodWithAuthAcceptance(uri, requestBody, bodyClass, NO_AUTHORIZATION);
    }

    <T> EntityExchangeResult<T> getMethodAcceptance(String uri, Class<T> bodyClass) {
        return getMethodWithAuthAcceptance(uri, bodyClass, NO_AUTHORIZATION);
    }

    <T> EntityExchangeResult<Void> deleteMethodAcceptance(String uri) {
        return deleteMethodWithAuthAcceptance(uri, NO_AUTHORIZATION);
    }

    <T> EntityExchangeResult<T> getMethodWithAuthAcceptance(String uri, Class<T> bodyClass, String jwt) {
        return this.webTestClient.get().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(bodyClass)
                .returnResult();
    }

    <T> EntityExchangeResult<T> postMethodWithAuthAcceptance(String uri, Object requestBody, Class<T> bodyClass, String jwt) {
        return webTestClient.post().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestBody), requestBody.getClass())
                .exchange()
                .expectBody(bodyClass)
                .returnResult();
    }

    <T> EntityExchangeResult<Void> deleteMethodWithAuthAcceptance(String uri, String jwt) {
        return this.webTestClient.delete().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .exchange()
                .expectBody(Void.class)
                .returnResult();
    }

    String createUser() {
        return Objects.requireNonNull(
                postMethodAcceptance(USER_BASE_URL + "/sigh-up", KIM_INPUT_JSON, User.class)
                .getResponseHeaders()
                .getLocation()
                .getPath());
    }

    Long createStation(String stationName, String jwt) {
        String inputJson = "{\"name\":\"" + stationName + "\"}";
        return Objects.requireNonNull(
                postMethodWithAuthAcceptance(STATION_URL, inputJson, StationResponseView.class, jwt)
                        .getResponseBody().getId());
    }

    Long createLine(String jwt) {
        return Objects.requireNonNull(
                postMethodWithAuthAcceptance(LINE_URL, LINE_INPUT_JSON, LineResponseView.class, jwt)
                        .getResponseBody().getId());
    }

    void createEdge(Long lineId, Long sourceStationId, Long targetStationId, int distance, String jwt) {
        String inputJson = "{\"sourceId\":" + sourceStationId + ",\"targetId\":" + targetStationId
                + ",\"distance\":" + distance + "}";

        Objects.requireNonNull(
                postMethodWithAuthAcceptance(LINE_URL + "/" + lineId + "/edges", inputJson, Void.class, jwt))
                .getResponseBody();
    }
}
