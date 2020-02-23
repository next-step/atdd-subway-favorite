package atdd.favorite.web;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.path.TestConstant.STATION_NAME;

public class FavoriteStationHttpTest {
    public static final String FAVORITE_STATION_BASE_URI = "/favorite-station";
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;
    private Long stationId;
    public WebTestClient webTestClient;

    public FavoriteStationHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        userHttpTest.createUser(EMAIL, NAME, PASSWORD);
    }

    public Long createFavoriteStation(String userEmail, Long favoriteStationId) {
        String token = jwtTokenProvider.createToken(EMAIL);
        this.stationId = stationHttpTest.createStation(STATION_NAME);
        CreateFavoriteStationRequestView request = new CreateFavoriteStationRequestView(stationId);
        //then
        return webTestClient.post().uri(FAVORITE_STATION_BASE_URI)
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
    }
}
