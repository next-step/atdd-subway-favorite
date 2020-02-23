package atdd.bookmark.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import atdd.bookmark.application.dto.BookmarkResponseView;
import atdd.bookmark.application.dto.BookmarkSimpleResponseView;
import reactor.core.publisher.Mono;

import static atdd.bookmark.TestConstant.*;

public class BookmarkHttpTest {
  private WebTestClient webTestClient;
  private String authToken;

  public BookmarkHttpTest(WebTestClient webTestClient, String authToken) {
    this.webTestClient = webTestClient;
    this.authToken = authToken;
  }

  public EntityExchangeResult<BookmarkSimpleResponseView> addBookmark(Long stationId) {
    String requestBody = "{" 
      + "\"sourceStationID\" : " + stationId.toString()
      + "}";

    return webTestClient.post().uri("/bookmark")
      .header(HttpHeaders.AUTHORIZATION, authToken)
      .contentType(MediaType.APPLICATION_JSON)
      .body(Mono.just(requestBody), String.class)
      .exchange()
      .expectStatus().isCreated()
      .expectBody(BookmarkSimpleResponseView.class)
      .returnResult();
  }

  public EntityExchangeResult<BookmarkSimpleResponseView> addBookmark(Long sourceStationID, Long targetStationID) {
    String requestBody = "{" 
      + "\"sourceStationID\" : " + sourceStationID.toString() + ","
      + "\"targetStationID\" : " + targetStationID.toString()
      + "}";

    return webTestClient.post().uri("/bookmark")
      .header(HttpHeaders.AUTHORIZATION, authToken)
      .contentType(MediaType.APPLICATION_JSON)
      .body(Mono.just(requestBody), String.class)
      .exchange()
      .expectStatus().isCreated()
      .expectBody(BookmarkSimpleResponseView.class)
      .returnResult();
  }

  public EntityExchangeResult<BookmarkResponseView> getBookmarks() {
    return webTestClient.get().uri(STATION_BOOKMARK_URL)
      .header(HttpHeaders.AUTHORIZATION, authToken)
      .exchange()
      .expectStatus().isOk()
      .expectBody(BookmarkResponseView.class)
      .returnResult();
  }

  public void deleteBookmarks(Long id) {
    webTestClient.delete().uri(STATION_BOOKMARK_URL + "/" + id.toString())
      .header(HttpHeaders.AUTHORIZATION, authToken)
      .exchange()
      .expectStatus().isNoContent();
  }
}
