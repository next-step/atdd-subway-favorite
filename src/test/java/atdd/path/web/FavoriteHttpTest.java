package atdd.path.web;

import atdd.path.application.dto.FavoriteStationResponseView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static atdd.path.TestConstant.JWT_TOKEN_TYPE;
import static atdd.path.application.base.BaseUriConstants.FAVORITE_BASE_URL;
import static atdd.path.application.base.BaseUriConstants.STATION_BASE_URL;

public class FavoriteHttpTest {

    public WebTestClient webTestClient;

    public FavoriteHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<FavoriteStationResponseView> createUserFavoriteStation(Long stationId,
                                                                                       String accessToken) {
        return webTestClient.post().uri(FAVORITE_BASE_URL + STATION_BASE_URL + "/" + stationId)
                .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN_TYPE +  accessToken)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody(FavoriteStationResponseView.class)
                .returnResult();
    }
}
