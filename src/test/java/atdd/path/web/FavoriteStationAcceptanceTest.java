package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.TestConstant;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.application.dto.LoginResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class FavoriteStationAcceptanceTest extends AbstractAcceptanceTest {

    private UserHttpTest userHttpTest;
    private final String FAVORITE_URI = "/favorites";

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("지하철역 즐겨찾기 등록")
    @Test
    void createStation() {
        userHttpTest.createUser(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN);
        LoginResponseView token = userHttpTest.loginUser(TestConstant.EMAIL_BROWN, TestConstant.PASSWORD_BROWN).getResponseBody();
        String request = "{\"stationId\": 1}";

        webTestClient.post().uri(FAVORITE_URI + "/station")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .body(Mono.just(request), String.class)
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().isCreated()
                .expectBody(FavoriteResponseView.class);
    }

}
