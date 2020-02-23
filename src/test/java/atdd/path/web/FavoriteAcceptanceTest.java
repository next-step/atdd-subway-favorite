package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.TestConstant;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.application.dto.LoginResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {

    private final String FAVORITE_URI = "/favorites";
    private UserHttpTest userHttpTest;
    private FavoriteHttpTest favoriteHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.favoriteHttpTest = new FavoriteHttpTest(webTestClient);
    }

    @DisplayName("지하철역 즐겨찾기 등록")
    @Test
    void createStation() {
        userHttpTest.createUser(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN);
        LoginResponseView token = userHttpTest.loginUser(TestConstant.EMAIL_BROWN, TestConstant.PASSWORD_BROWN).getResponseBody();

        FavoriteResponseView response = favoriteHttpTest.createFavoriteStation(1L, token).getResponseBody();

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("지하철역 즐겨찾기 조회")
    @Test
    void findStation() {
        // given
        userHttpTest.createUser(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN);
        LoginResponseView token = userHttpTest.loginUser(TestConstant.EMAIL_BROWN, TestConstant.PASSWORD_BROWN).getResponseBody();
        FavoriteResponseView response = favoriteHttpTest.createFavoriteStation(1L, token).getResponseBody();

        webTestClient.get().uri(FAVORITE_URI + "/station")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FavoriteResponseView.class);
    }
}
