package atdd.favorite.web;

import atdd.AbstractAcceptanceTest;
import atdd.favorite.application.dto.FavoritePathListResponseView;
import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.path.web.LineHttpTest;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoritePathAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_PATH_BASE_URI = "/favorite-paths";
    public static final String NAME = "브라운";
    public static final String EMAIL2 = "boorwonie2@email.com";
    public static final String EMAIL3 = "boorwonie3@email.com";
    public static final String PASSWORD = "subway";
    private String token;
    private Long stationId;
    private Long stationId2;
    private Long stationId3;
    private Long lineId;
    public UserHttpTest userHttpTest;
    public StationHttpTest stationHttpTest;
    public LineHttpTest lineHttpTest;
    public FavoritePathHttpTest favoritePathHttpTest;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.favoritePathHttpTest = new FavoritePathHttpTest(webTestClient);
        this.lineHttpTest = new LineHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);
        userHttpTest.createUser(EMAIL3, NAME, PASSWORD);
    }

    @Test
    void 지하철경로_즐겨찾기_등록_요청을_보낸다() throws Exception {
        //given
        int theNumberOfStationsInPath = 3;
        setUpForTest(EMAIL3);

        //when
        FavoritePathResponseView favoritePath
                = favoritePathHttpTest.createFavoritePath(stationId, stationId3, token);

        //then
        assertThat(favoritePath.getFavoritePathStations().size()).isEqualTo(theNumberOfStationsInPath);
        assertThat(favoritePath.getId()).isEqualTo(1L);
    }

    @Test
    void 지하철경로_즐겨찾기_삭제_요청을_보낸다() throws Exception {
        //given
        setUpForTest(EMAIL3);
        FavoritePathResponseView favoritePath
                = favoritePathHttpTest.createFavoritePath(stationId, stationId3, token);

        //when
        FavoritePathRequestView requestView = new FavoritePathRequestView(favoritePath.getId());

        //then
        webTestClient.delete().uri(FAVORITE_PATH_BASE_URI + "/" + favoritePath.getId())
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 지하철경로_즐겨찾기_목록_불러오기_요청을_보낸다() throws Exception {
        //given
        int theNumberOfFavoritePaths = 3;
        setUpForTest(EMAIL3);
        favoritePathHttpTest.createFavoritePath(stationId, stationId3, token);
        favoritePathHttpTest.createFavoritePath(stationId2, stationId3, token);
        favoritePathHttpTest.createFavoritePath(stationId3, stationId, token);

        //when
        FavoritePathRequestView requestView = new FavoritePathRequestView(EMAIL3);

        //then
        webTestClient.get().uri(FAVORITE_PATH_BASE_URI)
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FavoritePathListResponseView.class)
                .hasSize(theNumberOfFavoritePaths);
    }

    void setUpForTest(String email) {
        token = jwtTokenProvider.createToken(email);
        stationId = stationHttpTest.createStation(STATION_NAME);
        stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        lineId = lineHttpTest.createLine(LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3);
    }
}
