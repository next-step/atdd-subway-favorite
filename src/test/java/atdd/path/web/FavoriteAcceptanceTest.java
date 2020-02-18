package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.application.dto.FavoriteStationsResponseView;
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

    private MemberHttpTest memberHttpTest;
    private StationHttpTest stationHttpTest;

    @BeforeEach
    void setUp() {
        memberHttpTest = new MemberHttpTest(webTestClient);
        stationHttpTest = new StationHttpTest(webTestClient);
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

    private FavoriteStationResponseView createForStation(Long stationId, String token) {
        EntityExchangeResult<FavoriteStationResponseView> result = createForStationRequest(stationId, token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoriteStationResponseView> createForStationRequest(Long stationId, String token) {
        return webTestClient.post().uri(FAVORITES_STATIONS_URL + "/" + stationId)
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(FavoriteStationResponseView.class)
                .returnResult();
    }

    private FavoriteStationsResponseView findForStations(String token) {
        EntityExchangeResult<FavoriteStationsResponseView> result = findForStationRequest(token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoriteStationsResponseView> findForStationRequest(String token) {
        return webTestClient.get().uri(FAVORITES_STATIONS_URL)
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FavoriteStationsResponseView.class)
                .returnResult();
    }

}
