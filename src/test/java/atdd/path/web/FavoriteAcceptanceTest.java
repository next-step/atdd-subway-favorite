package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {

    private FavoriteHttpTest favoriteHttpTest;
    private MemberHttpTest memberHttpTest;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;
    private GraphHttpTest graphHttpTest;

    @BeforeEach
    void setUp() {
        favoriteHttpTest = new FavoriteHttpTest(webTestClient);
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

        FavoriteStationResponseView view = favoriteHttpTest.createForStation(stationId, token);

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

        favoriteHttpTest.createForStationRequest(stationId, token);
        favoriteHttpTest.createForStationRequest(stationId2, token);
        favoriteHttpTest.createForStationRequest(stationId3, token);

        FavoriteStationsResponseView view = favoriteHttpTest.findForStations(token);

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

        FavoriteStationResponseView createView = favoriteHttpTest.createForStation(stationId, token);

        webTestClient.delete().uri(FAVORITES_STATIONS_URL + "/" + createView.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isNoContent();

        FavoriteStationsResponseView findView = favoriteHttpTest.findForStations(token);

        assertThat(findView).isNotNull();
        assertThat(findView.getCount()).isEqualTo(0);
    }

    @DisplayName("경로 즐겨찾기 등록을 할 수 있다")
    @Test
    void beAbleToSaveForPath() {
        String token = setUpPath();

        PathResponseView findView = graphHttpTest.findPath(STATION_ID, STATION_ID_4);

        EntityExchangeResult<FavoritePathResponseView> result = favoriteHttpTest.createForPathRequest(
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

        favoriteHttpTest.createForPathRequest(STATION_ID, STATION_ID_3, token);
        favoriteHttpTest.createForPathRequest(STATION_ID_2, STATION_ID_4, token);
        favoriteHttpTest.createForPathRequest(STATION_ID, STATION_ID_4, token);

        FavoritePathsResponseView view = favoriteHttpTest.findForPaths(token);

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

        FavoritePathResponseView createView = favoriteHttpTest.createForPath(STATION_ID, STATION_ID_3, token);

        webTestClient.delete().uri(FAVORITES_PATH_URL + "/" + createView.getId())
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isNoContent();

        FavoritePathsResponseView findView = favoriteHttpTest.findForPaths(token);

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

}
