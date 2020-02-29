package atdd.favorite.web;

import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.application.dto.FavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.path.web.LineHttpTest;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.TestConstant.*;

public class FavoritePathAcceptanceTest {
    public static final String FAVORITE_PATH_BASE_URI = "/favorite-paths";
    public static final String NAME = "브라운";
    public static final String EMAIL2 = "boorwonie2@email.com";
    public static final String EMAIL3 = "boorwonie3@email.com";
    public static final String PASSWORD = "subway";
    private static String token;
    public WebTestClient webTestClient;
    private static UserHttpTest userHttpTest;
    private static StationHttpTest stationHttpTest;
    private static LineHttpTest lineHttpTest;

    public FavoritePathAcceptanceTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(){
        token=jwtTokenProvider.createToken(EMAIL3);
        this.userHttpTest=new UserHttpTest(webTestClient);
        this.lineHttpTest=new LineHttpTest(webTestClient);
        this.stationHttpTest=new StationHttpTest(webTestClient);
    }

    @Test
    public void create() {
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long stationId4 = stationHttpTest.createStation(STATION_NAME_4);
        Long lineId = lineHttpTest.createLine(LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3);
        lineHttpTest.createEdgeRequest(lineId, stationId3, stationId4);

        FavoritePathRequestView requestView
                = new FavoritePathRequestView(EMAIL3, stationId, stationId3);

        webTestClient.post().uri(FAVORITE_PATH_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestView), FavoritePathRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("email").exists()
                .jsonPath("startId").exists()
                .jsonPath("endId").exists();
    }
}
