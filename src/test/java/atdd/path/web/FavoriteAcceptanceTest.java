package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.FavoriteStationResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.TestConstant.TEST_USER;
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
        Long userId = userHttpTest.createUser(TEST_USER);
        String accessToken = loginHttpTest.getAccessTokenFromLogin(TEST_USER);
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        // when
        EntityExchangeResult<FavoriteStationResponseView> response = favoriteHttpTest.createUserFavoriteStation(
                stationId, accessToken);

        // then
        assertThat(response.getResponseHeaders().getLocation()).isNotNull();
        assertThat(response.getResponseBody()).isNotNull();
        assertThat(response.getResponseBody().getId()).isNotNull();
        assertThat(response.getResponseBody().getUser().getId()).isEqualTo(userId);
        assertThat(response.getResponseBody().getStation().getId()).isEqualTo(stationId);
    }
}
