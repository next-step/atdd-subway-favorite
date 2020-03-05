package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.favorite.FavoriteCreateResponseView;
import atdd.path.security.TokenAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.TestConstant.STATION_NAME_2;
import static atdd.path.dao.FavoriteDao.EDGE_TYPE;
import static atdd.path.dao.FavoriteDao.STATION_TYPE;
import static atdd.path.fixture.FavoriteFixture.EDGE_FAVORITE_CREATE_REQUEST_VIEW;
import static atdd.path.fixture.FavoriteFixture.STATION_FAVORITE_CREATE_REQUEST_VIEW;
import static atdd.path.fixture.UserFixture.KIM_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_BASE_URL = "/favorites";

    private CreateWebClientTest restWebClientTest;
    private TokenAuthenticationService tokenAuthenticationService;

    @BeforeEach
    void setUp() {
        cleanAllDatabases();
        this.restWebClientTest = new CreateWebClientTest(this.webTestClient);
        this.tokenAuthenticationService = new TokenAuthenticationService();
    }

    @DisplayName("지하철역 즐겨찾기 등록이 성공하는지")
    @Test
    public void createFavoriteToStation() {
        restWebClientTest.createUser();
        restWebClientTest.createStation(STATION_NAME, getJwt());

        //when
        WebTestClient.BodyContentSpec expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, STATION_FAVORITE_CREATE_REQUEST_VIEW, getJwt());

        expectResponse.jsonPath("$.item.name").isEqualTo(STATION_NAME);
    }

    @DisplayName("사용자가 등록된 지하철역 즐겨찾기를 조회되는지")
    @Test
    public void detailFavoriteToStation() {
        //given
        createStationFavorite();

        //when
        WebTestClient.BodyContentSpec expectResponse = restWebClientTest.getMethodWithAuthAcceptance
                (FAVORITE_BASE_URL + "?type=" + STATION_TYPE, getJwt());

        //then
        expectResponse.jsonPath("$.favorites[0].item.name").isEqualTo(STATION_NAME);
    }

    @DisplayName("사용자가 등록된 지하철역 즐겨찾기를 삭제 가능한지")
    @Test
    public void deleteFavoriteToStation() {
        //given
        String url = createStationFavorite();

        //when
        EntityExchangeResult<Void> expectResponse
                = restWebClientTest.deleteMethodWithAuthAcceptance(url, getJwt());

        //then
        assertThat(expectResponse.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("지하철 경로 즐겨찾기를 등록 가능한지")
    @Test
    public void createFavoriteToEdge() {
        restWebClientTest.createUser();
        Long sourceStationId = restWebClientTest.createStation(STATION_NAME, getJwt());
        Long targetStationId = restWebClientTest.createStation(STATION_NAME_2, getJwt());
        Long lineId = restWebClientTest.createLine(getJwt());
        restWebClientTest.createEdge(lineId, sourceStationId, targetStationId, 10, getJwt());

        //when
        WebTestClient.BodyContentSpec expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, EDGE_FAVORITE_CREATE_REQUEST_VIEW, getJwt());

        expectResponse.jsonPath("$.item.sourceStation.name").isEqualTo(STATION_NAME)
                .jsonPath("$.item.targetStation.name").isEqualTo(STATION_NAME_2);
    }

    @DisplayName("사용자가 등록된 지하철노선의 즐겨찾기를 조회되는지")
    @Test
    public void detailFavoriteToEdge() {
        //given
        createEdgeFavorite();

        //when
        WebTestClient.BodyContentSpec expectResponse = restWebClientTest.getMethodWithAuthAcceptance
                (FAVORITE_BASE_URL + "?type=" + EDGE_TYPE, getJwt());

        //then
        expectResponse.jsonPath("$.favorites[0].item.sourceStation.name").isEqualTo(STATION_NAME);
        expectResponse.jsonPath("$.favorites[0].item.targetStation.name").isEqualTo(STATION_NAME_2);
    }

    @DisplayName("사용자가 등록된 지하철 구간 즐겨찾기를 삭제 가능한지")
    @Test
    public void deleteFavoriteToEdge() {
        //given
        String url = createEdgeFavorite();

        //when
        EntityExchangeResult<Void> expectResponse
                = restWebClientTest.deleteMethodWithAuthAcceptance(url, getJwt());

        //then
        assertThat(expectResponse.getStatus()).isEqualTo(HttpStatus.OK);
    }

    String createEdgeFavorite() {
        //given
        restWebClientTest.createUser();
        Long sourceStationId = restWebClientTest.createStation(STATION_NAME, getJwt());
        Long targetStationId = restWebClientTest.createStation(STATION_NAME_2, getJwt());
        Long lineId = restWebClientTest.createLine(getJwt());
        restWebClientTest.createEdge(lineId, sourceStationId, targetStationId, 10, getJwt());

        //when
        EntityExchangeResult<FavoriteCreateResponseView> expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, EDGE_FAVORITE_CREATE_REQUEST_VIEW, FavoriteCreateResponseView.class, getJwt());

        return expectResponse
                .getResponseHeaders()
                .getLocation()
                .getPath();
    }

    String createStationFavorite() {
        //given
        restWebClientTest.createUser();
        restWebClientTest.createStation(STATION_NAME, getJwt());

        //when
        EntityExchangeResult<FavoriteCreateResponseView> expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, STATION_FAVORITE_CREATE_REQUEST_VIEW, FavoriteCreateResponseView.class, getJwt());

        return expectResponse
                .getResponseHeaders()
                .getLocation()
                .getPath();
    }

    private String getJwt() {
        return tokenAuthenticationService.toJwtByEmail(KIM_EMAIL);
    }
}
