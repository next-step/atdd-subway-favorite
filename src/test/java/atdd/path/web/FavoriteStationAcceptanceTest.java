package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.domain.FavoriteStation;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static atdd.path.TestConstant.CREATE_USER_REQUEST1;
import static atdd.path.TestConstant.STATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {
    private FavoriteHttpTest favoriteHttpTest;
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;
    private HttpTestUtils httpTestUtils;

    @BeforeEach
    void setUp() {
        this.httpTestUtils = new HttpTestUtils(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);

        this.stationHttpTest = new StationHttpTest(httpTestUtils);
        this.favoriteHttpTest = new FavoriteHttpTest(httpTestUtils);
    }

    @Test
    public void addFavoriteStation() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);

        //when
        FavoriteStation favoriteStation = favoriteHttpTest.createFavoriteStation(stationId, accessToken).getResponseBody();

        //then
        assertThat(favoriteStation.getStationId()).isEqualTo(stationId);
    }
}
