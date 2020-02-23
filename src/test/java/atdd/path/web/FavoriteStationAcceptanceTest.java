package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.domain.entity.FavoriteStation;
import atdd.path.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static atdd.path.TestConstant.CREATE_USER_REQUEST1;
import static atdd.path.TestConstant.STATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {
    private FavoriteHttpTest favoriteHttpTest;
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;

    @BeforeEach
    void setUp() {
        this.favoriteHttpTest = new FavoriteHttpTest(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
    }

    @Test
    public void createFavoriteStation() {
        //given
        String token = givenCreateAndLogin(CREATE_USER_REQUEST1);
        long stationId = stationHttpTest.createStation(STATION_NAME);

        //when
        FavoriteStation favoriteStation = favoriteHttpTest.createFavoriteStation(stationId, token).getResponseBody();

        //then
        assertThat(favoriteStation.getStationIds().size()).isEqualTo(1);
    }

    private String givenCreateAndLogin(CreateUserRequestView view) {
        User user = userHttpTest.createUserRequest(view).getResponseBody();

        return userHttpTest.givenLogin(user);
    }
}
