package atdd.favorite.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.TestConstant.STATION_NAME_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-stations";
    public static final String EMAIL = "boorwonie@email.com";
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;
    private FavoriteStationHttpTest favoriteStationHttpTest;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.favoriteStationHttpTest = new FavoriteStationHttpTest(webTestClient, jwtTokenProvider);
    }

    @Test
    public void createFavoriteStation() {
        //given
        String token = jwtTokenProvider.createToken(EMAIL);
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        //when
        Long favoriteStationId = favoriteStationHttpTest.createFavoriteStation(EMAIL, stationId, token);

        //then
        assertThat(1L).isEqualTo(favoriteStationId);
    }

    @Test
    public void deleteFavoriteStation() throws Exception {
        //given
        String token = jwtTokenProvider.createToken(EMAIL);
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long favoriteStationId = favoriteStationHttpTest.createFavoriteStation(EMAIL, stationId, token);

        //when, then
        mockMvc.perform(
                delete(FAVORITE_STATION_BASE_URI + "/" + favoriteStationId)
                        .header("Authorization", AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void showFavoriteStations() throws Exception {
        //given
        String token = jwtTokenProvider.createToken(EMAIL);
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        favoriteStationHttpTest.createFavoriteStation(EMAIL, stationId, token);
        favoriteStationHttpTest.createFavoriteStation(EMAIL, stationId2, token);

        //when, then
        mockMvc.perform(
                get(FAVORITE_STATION_BASE_URI)
                        .header("Authorization", AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andDo(print());
    }
}