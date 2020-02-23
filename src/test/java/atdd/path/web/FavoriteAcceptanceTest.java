package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.domain.Station;
import atdd.path.security.TokenAuthenticationService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.fixture.UserFixture.KIM_EMAIL;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_INPUT_JSON = "{\"name\":\"강남역\"}";
    public static final String FAVORITE_BASE_URL = "/favorite";

    private RestWebClientTest restWebClientTest;
    private TokenAuthenticationService tokenAuthenticationService;

    @BeforeEach
    void setUp() {
        this.restWebClientTest = new RestWebClientTest(this.webTestClient);
        this.tokenAuthenticationService = new TokenAuthenticationService();
    }

    @DisplayName("지하철역 즐겨찾기 등록이 성공하는지")
    @Test
    public void createFavoriteToStation(SoftAssertions softly) {
        //when
        EntityExchangeResult<Favorite> expectResponse
                = restWebClientTest.postMethodWithAuthAcceptance
                (FAVORITE_BASE_URL, FAVORITE_INPUT_JSON, Favorite.class, getJwt());

        //then
        HttpHeaders responseHeaders = expectResponse.getResponseHeaders();
        Favorite responseBody = expectResponse.getResponseBody();
        Station station = responseBody.getStation();

        //then
        softly.assertThat(responseHeaders.getLocation()).isNotNull();
        softly.assertThat(station.getName()).isEqualTo("강남역");
    }

    private String getJwt() {
        return tokenAuthenticationService.toJwtByEmail(KIM_EMAIL);
    }
}
