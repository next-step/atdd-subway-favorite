package atdd.favorite.web;

import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static atdd.Constant.AUTH_SCHEME_BEARER;

public class FavoritePathHttpTest {
    public static final String FAVORITE_PATH_BASE_URI = "/favorite-paths";
    public WebTestClient webTestClient;

    public FavoritePathHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public FavoritePathResponseView createFavoritePath(Long startId, Long endId, String token){
        FavoritePathRequestView requestView = new FavoritePathRequestView(startId, endId);
        return webTestClient.post().uri(FAVORITE_PATH_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestView), FavoritePathRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(FavoritePathResponseView.class)
                .getResponseBody()
                .toStream()
                .collect(Collectors.toList())
                .get(0);
    }
}
