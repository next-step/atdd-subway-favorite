package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import atdd.path.security.TokenAuthenticationService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.fixture.UserFixture.KIM_EMAIL;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_INPUT_JSON = "{\"stationId\":\"1\", \"stationName\":\"강남역\"}";
    public static final String FAVORITE_BASE_URL = "/favorites";

    private RestWebClientTest restWebClientTest;
    private TokenAuthenticationService tokenAuthenticationService;

    @BeforeEach
    void setUp() {
        cleanAllDatabases();
        this.restWebClientTest = new RestWebClientTest(this.webTestClient);
        this.tokenAuthenticationService = new TokenAuthenticationService();
    }

    @DisplayName("지하철역 즐겨찾기 등록이 성공하는지")
    @Test
    public void createFavoriteToStation(SoftAssertions softly) {
        restWebClientTest.createUser();
        restWebClientTest.createStation(STATION_NAME);

        //when
        EntityExchangeResult<Favorite> expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, FAVORITE_INPUT_JSON, Favorite.class, getJwt());

        //then
        HttpHeaders responseHeaders = expectResponse.getResponseHeaders();
        Favorite responseBody = expectResponse.getResponseBody();
        Station station = responseBody.getStation();

        //then
        softly.assertThat(responseHeaders.getLocation()).isNotNull();
        softly.assertThat(station.getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("사용자가 등록된 지하철역 즐겨찾기를 조호되는지")
    @Test
    public void detailFavoriteToStation(SoftAssertions softly) {
        //given
        restWebClientTest.createUser();
        restWebClientTest.createStation(STATION_NAME);
        createFavorite();

        //when
        EntityExchangeResult<FavoriteListResponseView> expectResponse
                = restWebClientTest.getMethodWithAuthAcceptance(FAVORITE_BASE_URL, FavoriteListResponseView.class, getJwt());

        //then
        FavoriteListResponseView responseBody = expectResponse.getResponseBody();
        Station station = responseBody.getFirstFavoriteStation();

        //then
        softly.assertThat(station.getName()).isEqualTo(STATION_NAME);
    }


    String createFavorite() {
        EntityExchangeResult<Favorite> expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, FAVORITE_INPUT_JSON, Favorite.class, getJwt());

        return expectResponse
                .getResponseHeaders()
                .getLocation()
                .getPath();
    }


    private String getJwt() {
        return tokenAuthenticationService.toJwtByEmail(KIM_EMAIL);
    }
}
