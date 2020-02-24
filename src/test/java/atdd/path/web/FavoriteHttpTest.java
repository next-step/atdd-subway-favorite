package atdd.path.web;

import atdd.path.domain.FavoriteStation;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

public class FavoriteHttpTest {
    final String FAVORITE_PATH = "/favorites";
    private HttpTestUtils httpTestUtils;

    public FavoriteHttpTest(HttpTestUtils httpTestUtils) {
        this.httpTestUtils = httpTestUtils;
    }

    public EntityExchangeResult<FavoriteStation> createFavoriteStation(long stationId, String accessToken) {
        return httpTestUtils.postRequest(FAVORITE_PATH, String.valueOf(stationId), accessToken, FavoriteStation.class);
    }
}
