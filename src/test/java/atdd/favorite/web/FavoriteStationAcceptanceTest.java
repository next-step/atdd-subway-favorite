package atdd.favorite.web;

import atdd.AbstractAcceptanceTest;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static atdd.TestConstant.STATION_NAME;
import static atdd.TestConstant.STATION_NAME_2;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-stations";
    public static final String NAME = "브라운";
    public static final String EMAIL2 = "boorwonie2@email.com";
    public static final String PASSWORD = "subway";
    private static UserHttpTest userHttpTest;
    private static StationHttpTest stationHttpTest;
    private static FavoriteStationHttpTest favoriteStationHttpTest;
    private String token;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.favoriteStationHttpTest = new FavoriteStationHttpTest(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        userHttpTest.createUser(EMAIL2, NAME, PASSWORD);
    }

    @Test
    public void 지하철역을_즐겨찾기에_등록한다() throws Exception {
        //given, when
        Long favoriteStationId = makeFavoriteStationForTest(EMAIL2, STATION_NAME);

        //then
        assertThat(favoriteStationId).isGreaterThan(0L);
    }

    @Test
    public void 지하철역_즐겨찾기를_삭제한다() throws Exception {
        //given
        Long favoriteStationId = makeFavoriteStationForTest(EMAIL2, STATION_NAME_2);

        //when
        webTestClient.delete().uri(FAVORITE_STATION_BASE_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    private Long makeFavoriteStationForTest(String email, String stationName) throws Exception {
        Long stationId = stationHttpTest.createStation(stationName);
        token = jwtTokenProvider.createToken(email);
        return favoriteStationHttpTest.createFavoriteStationHttpTest(email, stationId, token);
    }
}