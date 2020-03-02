package atdd.path.web;

import atdd.path.application.dto.PathResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

public class GraphHttpTest {
    public WebTestClient webTestClient;

    public GraphHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<PathResponseView> retrieveStationPath(Long stationId, Long stationId4) {
        return webTestClient.get().uri(uriBuilder -> uriBuilder.path("/paths")
                .queryParam("startId", stationId)
                .queryParam("endId", stationId4)
                .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PathResponseView.class)
                .returnResult();
    }
}
