package atdd.favorite.web;

import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    public FavoritePathResponseView createFavoritePath(String email,
                                                       Long startId,
                                                       Long endId,
                                                       String token) throws Exception {
        FavoritePathRequestView requestView = new FavoritePathRequestView(email, startId, endId);
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

    public FavoritePathResponseView deleteFavoritePath(Long id, String email, String token) {
        FavoritePathRequestView requestView = new FavoritePathRequestView(id, email);
        return webTestClient.delete().uri(FAVORITE_PATH_BASE_URI+"/"+id)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(FavoritePathResponseView.class)
                .getResponseBody()
                .toStream()
                .collect(Collectors.toList())
                .get(0);
    }
}
