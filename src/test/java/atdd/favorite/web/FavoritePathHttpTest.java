package atdd.favorite.web;

import atdd.favorite.application.dto.CreateFavoritePathRequestView;
import atdd.favorite.domain.FavoritePath;
import atdd.user.jwt.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static atdd.Constant.AUTH_SCHEME_BEARER;

public class FavoritePathHttpTest {
    public static final String FAVORITE_PATH_BASE_URI = "/favorite-paths";
    public WebTestClient webTestClient;
    public JwtTokenProvider jwtTokenProvider;

    public FavoritePathHttpTest(WebTestClient webTestClient, JwtTokenProvider jwtTokenProvider) {
        this.webTestClient = webTestClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long createFavoritePath(String email, Long startStationId, Long endStationId, String token) {
        CreateFavoritePathRequestView requestView
                = new CreateFavoritePathRequestView(email, startStationId, endStationId);
        return webTestClient.post().uri(FAVORITE_PATH_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestView), CreateFavoritePathRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(FavoritePath.class)
                .getResponseBody()
                .toStream()
                .map(FavoritePath::getId)
                .collect(Collectors.toList())
                .get(0);
    }
}
