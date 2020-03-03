package atdd.path.web;

import atdd.path.TestConstant;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.application.dto.FavoriteRouteResponseView;
import atdd.path.application.dto.LoginResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class FavoriteHttpTest {
    public static final String FAVORITE_URL = "/favorites";
    private final WebTestClient webTestClient;

    public FavoriteHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<FavoriteResponseView> createFavoriteStation(Long stationId, LoginResponseView token) {
        String request = "{\"stationId\": " + stationId + "}";

        return webTestClient.post().uri(FAVORITE_URL + "/station")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .body(Mono.just(request), String.class)
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().isCreated()
                .expectBody(FavoriteResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<FavoriteRouteResponseView> createFavoriteRoute(Long sourceStationId, Long targetStationId, LoginResponseView token) {
        String request = "{" +
                "\"sourceStationId\":" + sourceStationId + ", " +
                "\"targetStationId\":" + targetStationId + "}";

        return webTestClient.post().uri(FAVORITE_URL + "/route")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), String.class)
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().isCreated()
                .expectBody(FavoriteRouteResponseView.class)
                .returnResult();
    }
}
