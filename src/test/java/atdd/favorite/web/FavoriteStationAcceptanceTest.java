package atdd.favorite.web;

import atdd.AbstractAcceptanceTest;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static atdd.TestConstant.STATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String NAME = "브라운";
    public static final String EMAIL2 = "boorwonie2@email.com";
    public static final String PASSWORD = "subway";
    private static UserHttpTest userHttpTest;
    private static StationHttpTest stationHttpTest;
    private static FavoriteStationHttpTest favoriteStationHttpTest;
    private String token;

    @BeforeEach
    void setUp() {
        this.favoriteStationHttpTest = new FavoriteStationHttpTest(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        userHttpTest.createUser(EMAIL2, NAME, PASSWORD);
    }

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void 지하철역을_즐겨찾기에_등록한다() throws Exception {
        //given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        token = jwtTokenProvider.createToken(EMAIL2);

        //when
        Long id = favoriteStationHttpTest.createFavoriteStationHttpTest(EMAIL2, stationId, token);

        //then
        assertThat(id).isGreaterThan(0L);
    }
}