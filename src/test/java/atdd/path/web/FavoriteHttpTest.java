package atdd.path.web;

import atdd.path.domain.entity.FavoriteStation;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static atdd.path.TestConstant.*;

public class FavoriteHttpTest {
    final String FAVORITE_PATH = "/favorite";
    private WebTestClient webTestClient;

    public FavoriteHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<FavoriteStation> createFavoriteStation(long stationId, String accessToken) {
        return webTestClient.post().uri(FAVORITE_PATH)
                .header(ACCESS_TOKEN_HEADER)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(stationId), Long.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FavoriteStation.class)
                .returnResult();
    }
}
