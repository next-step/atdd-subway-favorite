package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
import atdd.path.domain.Favorite;
import atdd.path.domain.Item;
import atdd.path.security.TokenAuthenticationService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.fixture.UserFixture.KIM_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_STATION_INPUT_JSON = "{\"itemId\":\"1\", \"itemName\":\"강남역\", \"type\":\"station\"}";
    public static final String FAVORITE_LINE_INPUT_JSON = "{\"subwayId\":\"2\", \"lienName\":\"2호선\"}";
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
                (FAVORITE_BASE_URL, FAVORITE_STATION_INPUT_JSON, Favorite.class, getJwt());

        HttpHeaders responseHeaders = expectResponse.getResponseHeaders();
        Favorite responseBody = expectResponse.getResponseBody();
        Item station = responseBody.getItem();

        //then
        softly.assertThat(responseHeaders.getLocation()).isNotNull();
        softly.assertThat(station.getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("사용자가 등록된 지하철역 즐겨찾기를 조회되는지")
    @Test
    public void detailFavoriteToStation(SoftAssertions softly) {
        //given
        restWebClientTest.createUser();
        restWebClientTest.createStation(STATION_NAME);
        createFavorite();

        //when
        EntityExchangeResult<FavoriteListResponseView> expectResponse
                = restWebClientTest.getMethodWithAuthAcceptance(FAVORITE_BASE_URL, FavoriteListResponseView.class, getJwt());

        FavoriteListResponseView responseBody = expectResponse.getResponseBody();
        Item station = responseBody.getFirstFavoriteStation();

        //then
        softly.assertThat(station.getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("사용자가 등록된 지하철역 즐겨찾기를 삭제 가능한지")
    @Test
    public void deleteFavoriteToStation() {
        //given
        restWebClientTest.createUser();
        restWebClientTest.createStation(STATION_NAME);
        String url = createFavorite();

        //when
        EntityExchangeResult<Void> expectResponse
                = restWebClientTest.deleteMethodWithAuthAcceptance(url, getJwt());

        //then
        assertThat(expectResponse.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("경로 즐겨찾기를 등록 가능한지")
    @Test
    public void createFavoriteToGraph(SoftAssertions softly) {
        restWebClientTest.createUser();
        restWebClientTest.createStation(STATION_NAME);
        restWebClientTest.createLine();

        //when
        EntityExchangeResult<Favorite> expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, FAVORITE_LINE_INPUT_JSON, Favorite.class, getJwt());

        HttpHeaders responseHeaders = expectResponse.getResponseHeaders();
        Favorite responseBody = expectResponse.getResponseBody();
        Item graph = responseBody.getItem();

        //then
        softly.assertThat(responseHeaders.getLocation()).isNotNull();
        softly.assertThat(graph.getName()).isEqualTo(STATION_NAME);
    }


    String createFavorite() {
        EntityExchangeResult<Favorite> expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, FAVORITE_STATION_INPUT_JSON, Favorite.class, getJwt());

        return expectResponse
                .getResponseHeaders()
                .getLocation()
                .getPath();
    }


    private String getJwt() {
        return tokenAuthenticationService.toJwtByEmail(KIM_EMAIL);
    }
}
