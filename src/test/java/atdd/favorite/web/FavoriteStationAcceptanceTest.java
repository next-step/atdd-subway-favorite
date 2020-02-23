package atdd.favorite.web;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.path.TestConstant.STATION_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-station";
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;
    private Long stationId;
    private Long stationId2;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        userHttpTest.createUser(EMAIL, NAME, PASSWORD);
    }

    @Test
    public void createFavoriteStation() {
        //given
        String token = jwtTokenProvider.createToken(EMAIL);
        this.stationId = stationHttpTest.createStation(STATION_NAME);

        //when
        CreateFavoriteStationRequestView request = new CreateFavoriteStationRequestView(stationId);

        //then
        webTestClient.post().uri(FAVORITE_STATION_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateFavoriteStationRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody().jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.userEmail").isEqualTo(EMAIL)
                .jsonPath("$.favoriteStationId").isEqualTo(stationId);
    }

    @Test
    public void deleteFavoriteStation() throws Exception {
        //given
        String token = jwtTokenProvider.createToken(EMAIL);
        this.stationId = stationHttpTest.createStation(STATION_NAME);
        CreateFavoriteStationRequestView request = new CreateFavoriteStationRequestView(stationId);
        Long id = webTestClient.post().uri(FAVORITE_STATION_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateFavoriteStationRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(FavoriteStationResponseView.class)
                .getResponseBody()
                .toStream()
                .map(FavoriteStationResponseView::getId)
                .collect(Collectors.toList())
                .get(0);

        //when, then
        mockMvc.perform(
                delete(FAVORITE_STATION_BASE_URI + "/" + id)
                        .header("Authorization", AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void showFavoriteStations() throws Exception{
        //given
        String token = jwtTokenProvider.createToken(EMAIL);
        this.stationId = stationHttpTest.createStation(STATION_NAME);
        CreateFavoriteStationRequestView request = new CreateFavoriteStationRequestView(stationId);
        Long id = webTestClient.post().uri(FAVORITE_STATION_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateFavoriteStationRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(FavoriteStationResponseView.class)
                .getResponseBody()
                .toStream()
                .map(FavoriteStationResponseView::getId)
                .collect(Collectors.toList())
                .get(0);

        mockMvc.perform(
                        get(FAVORITE_STATION_BASE_URI+"/"+id)
                        .header("Authorization", AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationId").value(stationId))
                .andDo(print());
    }
}