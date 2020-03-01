package atdd.favorite.web;

import atdd.AbstractAcceptanceTest;
import atdd.favorite.application.dto.FavoriteStationListResponseVIew;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-stations";
    public static final String NAME = "브라운";
    public static final String EMAIL2 = "boorwonie2@email.com";
    public static final String EMAIL3 = "boorwonie3@email.com";
    public static final String PASSWORD = "subway";
    private static UserHttpTest userHttpTest;
    private static StationHttpTest stationHttpTest;
    private static FavoriteStationHttpTest favoriteStationHttpTest;
    private String token;
    private Long stationId;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.favoriteStationHttpTest = new FavoriteStationHttpTest(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        userHttpTest.createUser(EMAIL2, NAME, PASSWORD);
        userHttpTest.createUser(EMAIL3, NAME, PASSWORD);
    }

    @Test
    public void 지하철역을_즐겨찾기에_등록한다() throws Exception {
        //given, when
        Long favoriteStationId = makeFavoriteStationForTest(EMAIL2, STATION_NAME);

        //then
        assertThat(favoriteStationId).isEqualTo(1L);
    }

    @Test
    public void 지하철역_즐겨찾기를_삭제한다() throws Exception {
        //given
        Long favoriteStationId = makeFavoriteStationForTest(EMAIL2, STATION_NAME_2);

        //when, then
        webTestClient.delete().uri(FAVORITE_STATION_BASE_URI + "/" + favoriteStationId)
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    @Test
    void 지하철역_즐겨찾기_목록을_불러온다() throws Exception {
        //given
        int theNumberOfFavoriteStations = 3;
        makeFavoriteStationForTest(EMAIL2, STATION_NAME_3);
        makeFavoriteStationForTest(EMAIL2, STATION_NAME_4);
        makeFavoriteStationForTest(EMAIL2, STATION_NAME_5);

        //when, then
        webTestClient.get().uri(FAVORITE_STATION_BASE_URI)
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FavoriteStationListResponseVIew.class)
                .hasSize(theNumberOfFavoriteStations);
    }

    private Long makeFavoriteStationForTest(String email, String stationName) throws Exception {
        stationId = stationHttpTest.createStation(stationName);
        token = jwtTokenProvider.createToken(email);
        return favoriteStationHttpTest.createFavoriteStationHttpTest(stationId, token);
    }
}