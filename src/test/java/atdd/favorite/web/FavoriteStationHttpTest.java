package atdd.favorite.web;

import atdd.favorite.application.dto.FavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static atdd.Constant.AUTH_SCHEME_BEARER;

public class FavoriteStationHttpTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-stations";
    public WebTestClient webTestClient;

    public FavoriteStationHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public Long createFavoriteStationHttpTest(Long stationId, String token)
            throws Exception {
        FavoriteStationRequestView request
                = new FavoriteStationRequestView(stationId);
        return webTestClient.post().uri(FAVORITE_STATION_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FavoriteStationRequestView.class)
                .exchange()
                .returnResult(FavoriteStationResponseView.class)
                .getResponseBody()
                .toStream()
                .map(FavoriteStationResponseView::getId)
                .collect(Collectors.toList())
                .get(0);
    }
}
