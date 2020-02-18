package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.PathResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

public class GraphHttpTest extends AbstractAcceptanceTest {

    public WebTestClient webTestClient;

    public GraphHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public PathResponseView findPath(Long startId, Long endId) {
        EntityExchangeResult<PathResponseView> result = findPathRequest(startId, endId);
        return result.getResponseBody();
    }

    public EntityExchangeResult<PathResponseView> findPathRequest(Long startId, Long endId) {
        return webTestClient.get().uri("/paths?startId=" + startId + "&endId=" + endId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PathResponseView.class)
                .returnResult();
    }

}
