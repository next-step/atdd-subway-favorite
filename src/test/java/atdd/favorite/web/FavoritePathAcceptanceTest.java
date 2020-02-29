package atdd.favorite.web;

import atdd.AbstractAcceptanceTest;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.path.web.LineHttpTest;
import atdd.path.web.StationHttpTest;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

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
    public WebTestClient webTestClient;

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
                = favoritePathHttpTest.createFavoritePath(EMAIL3, stationId, stationId3, token);

        //then
        assertThat(favoritePath.getFavoritePathStations().size()).isEqualTo(theNumberOfStationsInPath);
        assertThat(favoritePath.getId()).isEqualTo(1L);
    }

    @Test
    void 지하철경로_즐겨찾기_삭제_요청을_보낸다() throws Exception{
        //given
        int theNumberOfStationsInPath = 3;
        setUpForTest(EMAIL3);
        FavoritePathResponseView favoritePath
                = favoritePathHttpTest.createFavoritePath(EMAIL3, stationId, stationId3, token);

        //when
        FavoritePathResponseView responseView
                = favoritePathHttpTest.deleteFavoritePath(favoritePath.getId(), EMAIL3, token);

        //then
        assertThat(responseView.getId()).isNull();
        assertThat(responseView.getFavoritePathStations()).isNullOrEmpty();
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
