package atdd.path.web;

import atdd.path.application.dto.FavoriteStationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

public class FavoriteHttpTest {
    final String FAVORITE_PATH = "/favorites";
    private HttpTestUtils httpTestUtils;

    public FavoriteHttpTest(HttpTestUtils httpTestUtils) {
        this.httpTestUtils = httpTestUtils;
    }

    public EntityExchangeResult<FavoriteStationResponse> createFavoriteStation(long stationId, String accessToken) {
        return httpTestUtils.postRequest(FAVORITE_PATH, String.valueOf(stationId), accessToken, FavoriteStationResponse.class);
    }

    public EntityExchangeResult<List<FavoriteStationResponse>> findFavoriteStations(String accessToken) {
        return httpTestUtils.getRequest(FAVORITE_PATH, accessToken, new ParameterizedTypeReference<List<FavoriteStationResponse>>() {
        });
    }

    public void deleteFavoriteStationById(long id, String accessToken) {
        httpTestUtils.deleteRequest(FAVORITE_PATH + "/" + id, accessToken);
    }
}
