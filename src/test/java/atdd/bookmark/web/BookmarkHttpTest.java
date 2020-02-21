package atdd.bookmark.web;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import atdd.bookmark.dto.StationBookmarkResponseView;
import atdd.bookmark.dto.StationBookmarkSimpleResponseView;
import reactor.core.publisher.Mono;

import static atdd.bookmark.TestConstant.*;

public class BookmarkHttpTest {
  private WebTestClient webTestClient;
  private String authToken;

  public BookmarkHttpTest(WebTestClient webTestClient, String authToken) {
    this.webTestClient = webTestClient;
    this.authToken = authToken;
  }

  public EntityExchangeResult<StationBookmarkSimpleResponseView> addStationBookmark(Long stationId) {
    String requestBody = "{" 
      + "\"sourceStationId\" : " + stationId.toString()
      + "}";

    return webTestClient.post().uri(STATION_BOOKMARK_URL)
      .header(HttpHeaders.AUTHORIZATION, authToken)
      .body(Mono.just(requestBody), String.class)
      .exchange()
      .expectStatus().isOk()
      .expectBody(StationBookmarkSimpleResponseView.class)
      .returnResult();
  }

  public EntityExchangeResult<StationBookmarkResponseView> getBookmarks() {
    return webTestClient.get().uri(STATION_BOOKMARK_URL)
      .header(HttpHeaders.AUTHORIZATION, authToken)
      .exchange()
      .expectStatus().isOk()
      .expectBody(StationBookmarkResponseView.class)
      .returnResult();
  }

  public void deleteBookmarks(Long id) {
    webTestClient.delete().uri(STATION_BOOKMARK_URL + "/" + id.toString())
      .header(HttpHeaders.AUTHORIZATION, authToken)
      .exchange()
      .expectStatus().isNoContent();
  }

  
}
