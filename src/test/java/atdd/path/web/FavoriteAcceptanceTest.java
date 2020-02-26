package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.FavoritePathRequestView;
import atdd.path.application.dto.FavoritePathResponseView;
import atdd.path.application.dto.FavoriteStationResponse;
import atdd.path.application.dto.PathResponseView;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {
    private FavoriteHttpTest favoriteHttpTest;
    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;
    private GraphHttpTest graphHttpTest;

    private HttpTestUtils httpTestUtils;

    @BeforeEach
    void setUp() {
        this.httpTestUtils = new HttpTestUtils(webTestClient);
        this.userHttpTest = new UserHttpTest(webTestClient);

        this.stationHttpTest = new StationHttpTest(httpTestUtils);
        this.favoriteHttpTest = new FavoriteHttpTest(httpTestUtils);
        this.lineHttpTest = new LineHttpTest(httpTestUtils);
        this.graphHttpTest = new GraphHttpTest(httpTestUtils);
    }

    @Test
    public void addFavoriteStation() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);

        //when
        FavoriteStationResponse favoriteStationResponse = favoriteHttpTest.createFavoriteStation(stationId, accessToken).getResponseBody();

        //then
        assertThat(favoriteStationResponse.getStation().getId()).isEqualTo(stationId);
    }

    @Test
    public void findFavoriteStations() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        FavoriteStationResponse favoriteStationResponse = favoriteHttpTest.createFavoriteStation(stationId, accessToken).getResponseBody();

        //when
        List<FavoriteStationResponse> favoriteStationResponses = favoriteHttpTest.findFavoriteStations(accessToken).getResponseBody();

        //then
        assertThat(favoriteStationResponses.size()).isEqualTo(1);
        assertThat(favoriteStationResponses.get(0).getStation().getId()).isEqualTo(favoriteStationResponse.getId());
    }

    @Test
    public void deleteFavoriteStation() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        long stationId = stationHttpTest.createStation(STATION_NAME, accessToken);
        FavoriteStationResponse favoriteStationResponse = favoriteHttpTest.createFavoriteStation(stationId, accessToken).getResponseBody();

        //when
        favoriteHttpTest.deleteFavoriteStationById(favoriteStationResponse.getId(), accessToken);

        //then
        List<FavoriteStationResponse> favoriteStationResponses = favoriteHttpTest.findFavoriteStations(accessToken).getResponseBody();

        assertThat(favoriteStationResponses.size()).isEqualTo(0);
    }

    @Test
    public void createFavoritePath() {
        //given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        long stationId1 = stationHttpTest.createStation(STATION_NAME_11, accessToken); //고속버스 터미널역
        long stationId2 = stationHttpTest.createStation(STATION_NAME_12, accessToken); //교대역
        long stationId3 = stationHttpTest.createStation(STATION_NAME, accessToken);    // 강남
        long stationId4 = stationHttpTest.createStation(STATION_NAME_2, accessToken);  // 역삼
        long stationId5 = stationHttpTest.createStation(STATION_NAME_3, accessToken);  // 선릉
        long stationId6 = stationHttpTest.createStation(STATION_NAME_4, accessToken);  // 삼성

        long lineId1 = lineHttpTest.createLine(LINE_NAME, accessToken);
        long lineId2 = lineHttpTest.createLine(LINE_NAME_3, accessToken);

        lineHttpTest.createEdgeRequest(lineId1, stationId2, stationId3, accessToken);
        lineHttpTest.createEdgeRequest(lineId1, stationId3, stationId4, accessToken);
        lineHttpTest.createEdgeRequest(lineId1, stationId4, stationId5, accessToken);
        lineHttpTest.createEdgeRequest(lineId1, stationId5, stationId6, accessToken);
        lineHttpTest.createEdgeRequest(lineId2, stationId1, stationId2, accessToken);

        //when
        PathResponseView pathResponseView = graphHttpTest.findPath(stationId1, stationId6, accessToken).getResponseBody();

        FavoritePathRequestView request = FavoritePathRequestView.builder()
                .sourceStationId(pathResponseView.getStartStationId())
                .targetStationId(pathResponseView.getEndStationId()).build();

        FavoritePathResponseView favoritePathResponseView = (FavoritePathResponseView) favoriteHttpTest.createFavoritePath(request, accessToken).getResponseBody();

        //then
        assertThat(favoritePathResponseView.getPaths().size()).isEqualTo(1);
        assertThat(favoritePathResponseView.getPaths().get(0).getStations().get(0).getName()).isEqualTo(TEST_STATION_11.getName());
        assertThat(favoritePathResponseView.getPaths().get(0).getStations().get(1).getName()).isEqualTo(TEST_STATION_4.getName());
    }

    @Test
    public void findFavoritePaths() {
        // given
        User givenUser = httpTestUtils.createGivenUser(CREATE_USER_REQUEST1);
        String accessToken = httpTestUtils.createGivenAccessToken(givenUser);

        long stationId1 = stationHttpTest.createStation(STATION_NAME_11, accessToken); //고속버스 터미널역
        long stationId2 = stationHttpTest.createStation(STATION_NAME_12, accessToken); //교대역
        long stationId3 = stationHttpTest.createStation(STATION_NAME, accessToken);    // 강남
        long stationId4 = stationHttpTest.createStation(STATION_NAME_2, accessToken);  // 역삼
        long stationId5 = stationHttpTest.createStation(STATION_NAME_3, accessToken);  // 선릉
        long stationId6 = stationHttpTest.createStation(STATION_NAME_4, accessToken);  // 삼성

        long lineId1 = lineHttpTest.createLine(LINE_NAME, accessToken);
        long lineId2 = lineHttpTest.createLine(LINE_NAME_3, accessToken);

        lineHttpTest.createEdgeRequest(lineId1, stationId2, stationId3, accessToken);
        lineHttpTest.createEdgeRequest(lineId1, stationId3, stationId4, accessToken);
        lineHttpTest.createEdgeRequest(lineId1, stationId4, stationId5, accessToken);
        lineHttpTest.createEdgeRequest(lineId1, stationId5, stationId6, accessToken);
        lineHttpTest.createEdgeRequest(lineId2, stationId1, stationId2, accessToken);

        PathResponseView pathResponseView = graphHttpTest.findPath(stationId1, stationId6, accessToken).getResponseBody();

        FavoritePathRequestView request = FavoritePathRequestView.builder()
                .sourceStationId(pathResponseView.getStartStationId())
                .targetStationId(pathResponseView.getEndStationId()).build();

        FavoritePathResponseView createFavorite = (FavoritePathResponseView) favoriteHttpTest.createFavoritePath(request, accessToken).getResponseBody();

        // when
        List<FavoritePathResponseView> favoritePathResponseViews = (List<FavoritePathResponseView>) favoriteHttpTest.findFavoritePath(accessToken).getResponseBody();

        // then
        assertThat(favoritePathResponseViews.size()).isEqualTo(1);
        assertThat(favoritePathResponseViews.get(0).getId()).isEqualTo(createFavorite.getId());
    }
}
