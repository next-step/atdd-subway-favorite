package atdd.favorite.web;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.user.jwt.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static atdd.Constant.AUTH_SCHEME_BEARER;

public class FavoriteStationHttpTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-stations";
    public WebTestClient webTestClient;
    public JwtTokenProvider jwtTokenProvider;

    public FavoriteStationHttpTest(WebTestClient webTestClient, JwtTokenProvider jwtTokenProvider) {
        this.webTestClient = webTestClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long createFavoriteStation(String userEmail, Long stationId, String token){
        CreateFavoriteStationRequestView request = new CreateFavoriteStationRequestView(stationId);
        return
                webTestClient.post().uri(FAVORITE_STATION_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateFavoriteStationRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(FavoriteStationResponseView.class)
                .getResponseBody()
                .toStream()
                .map(FavoriteStationResponseView::getId)
                .collect(Collectors.toList())
                .get(0);
    }
}
