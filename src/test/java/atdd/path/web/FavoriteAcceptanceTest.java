package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.FavoriteStationResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {

    private LoginHttpTest loginHttpTest;
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;
    private FavoriteHttpTest favoriteHttpTest;

    @BeforeEach
    void setUp() {
        this.loginHttpTest = new LoginHttpTest(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.favoriteHttpTest = new FavoriteHttpTest(webTestClient);

        userHttpTest.createUser(TEST_USER);
        loginHttpTest.getAccessTokenFromLogin(TEST_USER);
    }

    /**
     * Scenario: 지하철역 즐겨찾기 등록
     * Given 사용자는 로그인을 했다.
     * When 사용자는 지하철역 즐겨찾기 등록을 요청한다.
     * Then 지하철역 즐겨찾기가 등록되었다.
     */
    @Test
    @DisplayName("지하철역 즐겨찾기 등록")
    void createUserFavoriteStation() {
        // given
        String accessToken = loginHttpTest.getAccessTokenFromLogin(TEST_USER);
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        // when
        EntityExchangeResult<FavoriteStationResponseView> response =
                favoriteHttpTest.createUserFavoriteStationRequest(stationId, accessToken);

        // then
        assertThat(response.getResponseHeaders().getLocation()).isNotNull();
        assertThat(response.getResponseBody()).isNotNull();
        assertThat(response.getResponseBody().getId()).isNotNull();
        assertThat(response.getResponseBody().getStation().getId()).isEqualTo(stationId);
    }

    /**
     * Scenario: 지하철역 즐겨찾기 조회
     * Given 지하철역 즐겨찾기가 등록되어 있다.
     * And 사용자는 로그인을 했다.
     * When 사용자는 자신이 등록한 경로 즐겨찾기 목록을 요청한다.
     * Then 사용자는 지하철역 즐겨찾기 목록을 응답받는다.
     */
    @Test
    @DisplayName("지하철역 즐겨찾기 조회")
    void showUserFavoriteStations() {
        // given
        String accessToken = loginHttpTest.getAccessTokenFromLogin(TEST_USER);
        Long stationId1 = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long stationId4 = stationHttpTest.createStation(STATION_NAME_4);
        Long stationId5 = stationHttpTest.createStation(STATION_NAME_5);

        // when
        favoriteHttpTest.createUserFavoriteStation(stationId1, accessToken);
        favoriteHttpTest.createUserFavoriteStation(stationId2, accessToken);
        favoriteHttpTest.createUserFavoriteStation(stationId3, accessToken);
        favoriteHttpTest.createUserFavoriteStation(stationId4, accessToken);
        favoriteHttpTest.createUserFavoriteStation(stationId5, accessToken);

        EntityExchangeResult<List<FavoriteStationResponseView>> favoriteStations =
                favoriteHttpTest.showUserFavoriteStations(accessToken);

        // then
        assertThat(favoriteStations).isNotNull();
        assertThat(favoriteStations.getResponseBody().get(0)).isNotNull();
        assertThat(favoriteStations.getResponseBody().get(0).getId()).isEqualTo(stationId1);
        assertThat(favoriteStations.getResponseBody().size()).isEqualTo(5);
    }

    /**
     * Scenario: 지하철역 즐겨찾기 삭제
     * Given 지하철역 즐겨찾기가 등록되어 있다.
     * When 사용자는 지하철역 즐겨찾기 삭제를 요청한다.
     * Then 지하철역 즐겨찾기가 삭제되었다.
     */
    @Test
    @DisplayName("지하철역 즐겨찾기 삭제")
    void deleteFavoriteStation() {
        // given
        String accessToken = loginHttpTest.getAccessTokenFromLogin(TEST_USER);
        Long stationId1 = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long favoriteId1 = favoriteHttpTest.createUserFavoriteStation(stationId1, accessToken);
        Long favoriteId2 = favoriteHttpTest.createUserFavoriteStation(stationId2, accessToken);
        Long favoriteId3 = favoriteHttpTest.createUserFavoriteStation(stationId3, accessToken);

        System.out.println(stationId1 + " " +  stationId2 + " " + stationId3);
        System.out.println(favoriteId1 + " " +  favoriteId2 + " " + favoriteId3);

        // when
        favoriteHttpTest.deleteFavoriteStation(favoriteId1, accessToken);
        favoriteHttpTest.deleteFavoriteStation(favoriteId2, accessToken);

        // then
        EntityExchangeResult<List<FavoriteStationResponseView>> result =
                favoriteHttpTest.showUserFavoriteStations(accessToken);

        assertThat(result.getResponseBody().size()).isEqualTo(1);
    }
}
