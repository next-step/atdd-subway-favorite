package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.*;
import static atdd.path.application.provider.JwtTokenProvider.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {

    private final String FAVORITES_STATIONS_URL = "/favorites/stations";
    private final String FAVORITES_PATH_URL = "/favorites/paths";

    private MemberHttpTest memberHttpTest;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;
    private GraphHttpTest graphHttpTest;

    @BeforeEach
    void setUp() {
        memberHttpTest = new MemberHttpTest(webTestClient);
        stationHttpTest = new StationHttpTest(webTestClient);
        lineHttpTest = new LineHttpTest(webTestClient);
        graphHttpTest = new GraphHttpTest(webTestClient);
    }

    @DisplayName("지하철역 즐겨찾기 등록을 할 수 있다")
    @Test
    void beAbleToSaveForStation() {
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        memberHttpTest.createMemberRequest(TEST_MEMBER);
        String token = memberHttpTest.loginMember(TEST_MEMBER);

        FavoriteStationResponseView view = createForStation(stationId, token);

        assertThat(view).isNotNull();
        assertThat(view.getId()).isEqualTo(FAVORITE_STATION_ID);
        assertThat(view.getStation().getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("지하철역 즐겨찾기 목록을 조회 할 수 있다")
    @Test
    void beAbleToFindForStation() {
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);

        memberHttpTest.createMemberRequest(TEST_MEMBER);
        String token = memberHttpTest.loginMember(TEST_MEMBER);

        createForStationRequest(stationId, token);
        createForStationRequest(stationId2, token);
        createForStationRequest(stationId3, token);

        FavoriteStationsResponseView view = findForStations(token);

        assertThat(view).isNotNull();
        assertThat(view.getCount()).isEqualTo(3);
        assertThat(view.getFavorites())
                .extracting("station.name")
                .containsExactly(STATION_NAME, STATION_NAME_2, STATION_NAME_3);
    }

    @DisplayName("지하철역 즐겨찾기 삭제를 할 수 있다")
    @Test
    void beAbleToDeleteForStation() {
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        memberHttpTest.createMemberRequest(TEST_MEMBER);
        String token = memberHttpTest.loginMember(TEST_MEMBER);

        FavoriteStationResponseView createView = createForStation(stationId, token);

        webTestClient.delete().uri(FAVORITES_STATIONS_URL + "/" + createView.getId())
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus().isNoContent();

        FavoriteStationsResponseView findView = findForStations(token);

        assertThat(findView).isNotNull();
        assertThat(findView.getCount()).isEqualTo(0);
    }

    @DisplayName("경로 즐겨찾기 등록을 할 수 있다")
    @Test
    void beAbleToSaveForPath() {
        String token = setUpPath();

        PathResponseView findView = graphHttpTest.findPath(STATION_ID, STATION_ID_4);

        EntityExchangeResult<FavoritePathResponseView> result = createForPathRequest(
                findView.getStartStationId(),
                findView.getEndStationId(),
                token);

        FavoritePathResponseView favoriteView = result.getResponseBody();
        PathResponseView pathView = favoriteView.getPath();

        assertThat(favoriteView).isNotNull();
        assertThat(pathView.getStartStationId()).isEqualTo(STATION_ID);
        assertThat(pathView.getEndStationId()).isEqualTo(STATION_ID_4);
        assertThat(pathView.getStations().size()).isEqualTo(4);
    }

    @DisplayName("경로 즐겨찾기 목록을 조회 할 수 있다.")
    @Test
    void beAbleToFindForPath() {
        String token = setUpPath();

        createForPathRequest(STATION_ID, STATION_ID_3, token);
        createForPathRequest(STATION_ID_2, STATION_ID_4, token);
        createForPathRequest(STATION_ID, STATION_ID_4, token);

        FavoritePathsResponseView view = findForPaths(token);

        assertThat(view).isNotNull();
        assertThat(view.getCount()).isEqualTo(3);
        assertThat(view.getFavorites().get(0))
                .extracting("path.startStationId", "path.endStationId")
                .containsExactly(STATION_ID, STATION_ID_3);
    }

    @DisplayName("경로 즐겨찾기 삭제를 할 수 있다")
    @Test
    void beAbleToDeleteForPath() {
        String token = setUpPath();

        FavoritePathResponseView createView = createForPath(STATION_ID, STATION_ID_3, token);

        webTestClient.delete().uri(FAVORITES_PATH_URL + "/" + createView.getId())
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus().isNoContent();

        FavoritePathsResponseView findView = findForPaths(token);

        assertThat(findView).isNotNull();
        assertThat(findView.getCount()).isEqualTo(0);
    }

    private String setUpPath() {
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long stationId4 = stationHttpTest.createStation(STATION_NAME_4);
        Long lineId = lineHttpTest.createLine(LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3);
        lineHttpTest.createEdgeRequest(lineId, stationId3, stationId4);

        memberHttpTest.createMemberRequest(TEST_MEMBER);
        return memberHttpTest.loginMember(TEST_MEMBER);
    }

    private FavoriteStationResponseView createForStation(Long stationId, String token) {
        EntityExchangeResult<FavoriteStationResponseView> result = createForStationRequest(stationId, token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoriteStationResponseView> createForStationRequest(Long stationId, String token) {
        return createRequest(FavoriteStationResponseView.class, FAVORITES_STATIONS_URL + "/" + stationId, token);
    }

    private FavoriteStationsResponseView findForStations(String token) {
        EntityExchangeResult<FavoriteStationsResponseView> result = findForStationRequest(token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoriteStationsResponseView> findForStationRequest(String token) {
        return findRequest(FavoriteStationsResponseView.class, FAVORITES_STATIONS_URL, token);
    }

    private FavoritePathResponseView createForPath(Long startId, Long endId, String token) {
        EntityExchangeResult<FavoritePathResponseView> result = createForPathRequest(startId, endId, token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoritePathResponseView> createForPathRequest(Long startId, Long endId, String token) {
        return createRequest(FavoritePathResponseView.class,
                FAVORITES_PATH_URL + "?startId=" + startId + "&endId=" + endId, token);
    }

    private FavoritePathsResponseView findForPaths(String token) {
        EntityExchangeResult<FavoritePathsResponseView> result = findForPathsRequest(token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoritePathsResponseView> findForPathsRequest(String token) {
        return findRequest(FavoritePathsResponseView.class, FAVORITES_PATH_URL, token);
    }

    private <T> EntityExchangeResult<T> createRequest(Class<T> classT, String uri, String token) {
        return webTestClient.post().uri(uri)
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(classT)
                .returnResult();
    }

    private <T> EntityExchangeResult<T> findRequest(Class<T> classT, String uri, String token) {
        return webTestClient.get().uri(uri)
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(classT)
                .returnResult();
    }

}
