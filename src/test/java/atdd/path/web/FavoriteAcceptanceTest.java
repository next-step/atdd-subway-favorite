package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.TestConstant;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.domain.Station;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {

    private final String FAVORITE_URI = "/favorites";

    private UserHttpTest userHttpTest;
    private FavoriteHttpTest favoriteHttpTest;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;

    private LoginResponseView token;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.favoriteHttpTest = new FavoriteHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.lineHttpTest = new LineHttpTest(webTestClient);

        userHttpTest.createUser(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN);
        this.token = userHttpTest.loginUser(TestConstant.EMAIL_BROWN, TestConstant.PASSWORD_BROWN).getResponseBody();
    }

    @DisplayName("지하철역 즐겨찾기 등록")
    @Test
    void createFavoriteStation() {
        stationHttpTest.createStation(TestConstant.STATION_NAME);

        FavoriteResponseView response = favoriteHttpTest.createFavoriteStation(1L, token).getResponseBody();

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("지하철역 즐겨찾기 조회")
    @Test
    void findFavoriteStation() {
        // given
        stationHttpTest.createStation(TestConstant.STATION_NAME);
        FavoriteResponseView response = favoriteHttpTest.createFavoriteStation(1L, token).getResponseBody();

        webTestClient.get().uri(FAVORITE_URI + "/station")
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("지하철역 즐겨찾기 삭제")
    @Test
    void deleteFavoriteStation() {
        // given
        stationHttpTest.createStation(TestConstant.STATION_NAME);
        FavoriteResponseView response = favoriteHttpTest.createFavoriteStation(1L, token).getResponseBody();

        webTestClient.delete().uri(FAVORITE_URI + "/station/" + response.getId())
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectStatus().isNoContent();
    }

    @DisplayName("경로 즐겨찾기 등록")
    @Test
    void createFavoriteRoute() {
        //given
        Long firstStationId = stationHttpTest.createStation(TestConstant.STATION_NAME);
        Long secondStationId = stationHttpTest.createStation(TestConstant.STATION_NAME_2);
        Long lineId = lineHttpTest.createLine(TestConstant.LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, firstStationId, secondStationId);

        String request = "{" +
                "\"sourceStationId\":" + firstStationId + ", " +
                "\"targetStationId\":" + secondStationId + "}";

        webTestClient.post().uri(FAVORITE_URI + "/route")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), String.class)
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().isCreated();
    }
}
