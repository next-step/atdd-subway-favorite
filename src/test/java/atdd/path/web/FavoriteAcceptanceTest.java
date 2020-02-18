package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.TestConstant.TEST_MEMBER;
import static atdd.path.application.provider.JwtTokenProvider.AUTHORIZATION;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {

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

        webTestClient.post().uri("/favorites/stations/" + stationId)
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.station.name").isEqualTo(STATION_NAME);
    }

}
