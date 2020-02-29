package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.favorite.FavoriteCreateResponseView;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
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
    public void createFavoriteToStation(SoftAssertions softly) {
        restWebClientTest.createUser();
        restWebClientTest.createStation(STATION_NAME, getJwt());

        //when
        EntityExchangeResult<FavoriteCreateResponseView> expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, STATION_FAVORITE_CREATE_REQUEST_VIEW, FavoriteCreateResponseView.class, getJwt());

        HttpHeaders responseHeaders = expectResponse.getResponseHeaders();
        FavoriteCreateResponseView responseBody = expectResponse.getResponseBody();
        Item station = responseBody.getItem();

        //then
        softly.assertThat(responseHeaders.getLocation()).isNotNull();
        softly.assertThat(station.getId()).isNotNull();
    }

    @DisplayName("사용자가 등록된 지하철역 즐겨찾기를 조회되는지")
    @Test
    public void detailFavoriteToStation(SoftAssertions softly) {
        //given
        createStationFavorite();

        //when
        EntityExchangeResult<FavoriteListResponseView> expectResponse
                = restWebClientTest.getMethodWithAuthAcceptance
                (FAVORITE_BASE_URL + "?type=" + STATION_TYPE, FavoriteListResponseView.class, getJwt());

        FavoriteListResponseView responseBody = expectResponse.getResponseBody();
        Item station = responseBody.getFirstFavoriteItem();

        //then
        softly.assertThat(station.getId()).isNotNull();
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
    public void createFavoriteToEdge(SoftAssertions softly) {
        restWebClientTest.createUser();
        Long sourceStationId = restWebClientTest.createStation(STATION_NAME, getJwt());
        Long targetStationId = restWebClientTest.createStation(STATION_NAME_2, getJwt());
        Long lineId = restWebClientTest.createLine(getJwt());
        restWebClientTest.createEdge(lineId, sourceStationId, targetStationId, 10, getJwt());

        //when
        EntityExchangeResult<FavoriteCreateResponseView> expectResponse = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, EDGE_FAVORITE_CREATE_REQUEST_VIEW, FavoriteCreateResponseView.class, getJwt());

        HttpHeaders responseHeaders = expectResponse.getResponseHeaders();
        FavoriteCreateResponseView responseBody = expectResponse.getResponseBody();
        Item edge = responseBody.getItem();

        //then
        softly.assertThat(responseHeaders.getLocation()).isNotNull();
        softly.assertThat(edge.getId()).isNotNull();
    }

    @DisplayName("사용자가 등록된 지하철노선의 즐겨찾기를 조회되는지")
    @Test
    public void detailFavoriteToEdge(SoftAssertions softly) {
        //given
        createEdgeFavorite();

        //when
        EntityExchangeResult<FavoriteListResponseView> expectResponse
                = restWebClientTest.getMethodWithAuthAcceptance
                (FAVORITE_BASE_URL + "?type=" + EDGE_TYPE, FavoriteListResponseView.class, getJwt());

        FavoriteListResponseView responseBody = expectResponse.getResponseBody();
        Item item = responseBody.getFirstFavoriteItem();

        //then
        softly.assertThat(item.getId()).isNotNull();
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
