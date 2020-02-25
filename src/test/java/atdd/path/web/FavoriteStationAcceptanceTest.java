package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.FavoriteStationResponse;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        FavoriteStationResponse favoriteStationResponse = favoriteHttpTest.createFavoriteStation(stationId, accessToken).getResponseBody();

        //then
        assertThat(favoriteStationResponse.getStation().getId()).isEqualTo(stationId);
    }

    @Test
    public void findFavoriteStations() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        FavoriteStationResponse favoriteStationResponse = favoriteHttpTest.createFavoriteStation(stationId, accessToken).getResponseBody();

        //when
        List<FavoriteStationResponse> favoriteStationResponses = favoriteHttpTest.findFavoriteStations(accessToken).getResponseBody();

        //then
        assertThat(favoriteStationResponses.size()).isEqualTo(1);
        assertThat(favoriteStationResponses.get(0).getStation().getId()).isEqualTo(favoriteStationResponse.getId());
    }

    @Test
    public void deleteFavoriteStation() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        FavoriteStationResponse favoriteStationResponse = favoriteHttpTest.createFavoriteStation(stationId, accessToken).getResponseBody();

        //when
        favoriteHttpTest.deleteFavoriteStationById(favoriteStationResponse.getId(), accessToken);

        //then
        List<FavoriteStationResponse> favoriteStationResponses = favoriteHttpTest.findFavoriteStations(accessToken).getResponseBody();

        assertThat(favoriteStationResponses.size()).isEqualTo(0);
    }
}
