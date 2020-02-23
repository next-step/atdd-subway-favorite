package atdd.favorite.web;

import atdd.favorite.application.dto.CreateFavoritePathRequestView;
import atdd.path.AbstractAcceptanceTest;
import atdd.path.web.LineHttpTest;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.path.TestConstant.*;

@AutoConfigureMockMvc
public class FavoritePathAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_PATH_BASE_URI = "/favorite-paths";
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;
    private Long stationId;
    private Long stationId2;
    private Long stationId3;
    private Long stationId4;
    private Long lineId;
    private String token;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.token = jwtTokenProvider.createToken(EMAIL);
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.lineHttpTest = new LineHttpTest(webTestClient);
        this.stationId = stationHttpTest.createStation(STATION_NAME);
        this.stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        this.stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        this.stationId4 = stationHttpTest.createStation(STATION_NAME_4);
        this.lineId = lineHttpTest.createLine(LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3);
        lineHttpTest.createEdgeRequest(lineId, stationId3, stationId4);
    }

    @Test
    public void createFavoritePath() {
        CreateFavoritePathRequestView requestView
                = new CreateFavoritePathRequestView(EMAIL, stationId, stationId4);
        webTestClient.post().uri(URI.create(FAVORITE_PATH_BASE_URI))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody().jsonPath("$.userEmail").isEqualTo(EMAIL)
                .jsonPath("$.startStationId").isEqualTo(stationId)
                .jsonPath("$.endStationId").isEqualTo(stationId4);
    }
}
