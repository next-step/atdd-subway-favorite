package atdd.path.web;

import atdd.path.application.dto.FavoriteStationResponseView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static atdd.path.TestConstant.JWT_TOKEN_TYPE;
import static atdd.path.application.base.BaseUriConstants.FAVORITE_BASE_URL;
import static atdd.path.application.base.BaseUriConstants.STATION_BASE_URL;

public class FavoriteHttpTest {

    public WebTestClient webTestClient;

    public FavoriteHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<FavoriteStationResponseView> createUserFavoriteStationRequest(Long stationId, String accessToken) {
        return webTestClient.post().uri(FAVORITE_BASE_URL + STATION_BASE_URL + "/" + stationId)
                .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN_TYPE +  accessToken)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody(FavoriteStationResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<FavoriteStationResponseView> retrieveFavoriteStation(Long id, String accessToken) {
        return webTestClient.get().uri(FAVORITE_BASE_URL + STATION_BASE_URL + "/" + id)
                .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN_TYPE + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FavoriteStationResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<List<FavoriteStationResponseView>> showUserFavoriteStations(String accessToken) {
        return webTestClient.get().uri(FAVORITE_BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN_TYPE + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(FavoriteStationResponseView.class)
                .returnResult();
    }

    public void deleteFavoriteStation(Long favoriteId, String accessToken) {
        webTestClient.delete().uri(FAVORITE_BASE_URL + "/" + favoriteId)
                .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN_TYPE + accessToken)
                .exchange()
                .expectStatus().isNoContent();
    }

    public Long createUserFavoriteStation(Long stationId, String accessToken) {
        EntityExchangeResult<FavoriteStationResponseView> favoriteStationResponse =
                createUserFavoriteStationRequest(stationId, accessToken);

        return favoriteStationResponse.getResponseBody().getId();
    }
}
