package atdd.path.web;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import atdd.path.application.dto.PathResponseView;

public class GraphHttpTest {
  public WebTestClient webTestClient;

  public GraphHttpTest(WebTestClient webTestClient) {
    this.webTestClient = webTestClient;
  }

  public EntityExchangeResult<PathResponseView> findPath(Long startID, Long endID) {
    return webTestClient.get().uri("/paths?startId=" + startID + "&endId=" + endID)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody(PathResponseView.class)
      .returnResult();
  }
  
}
