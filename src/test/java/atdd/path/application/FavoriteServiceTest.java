package atdd.path.application;

import atdd.path.SoftAssertionTest;
import atdd.path.dao.UserDao;
import atdd.path.domain.Favorite;
import atdd.path.security.TokenAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.TestConstant.TEST_STATION;
import static atdd.path.fixture.FavoriteFixture.NEW_FAVORITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FavoriteServiceTest extends SoftAssertionTest {
    private FavoriteService favoriteService;
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private TokenAuthenticationService tokenAuthenticationService;

    @BeforeEach
    void setUp() {
        this.tokenAuthenticationService = new TokenAuthenticationService();
        this.userService = new UserService(tokenAuthenticationService, userDao);
    }

    @DisplayName("사용자가 지하철 등록에 성공하는지")
    @Test
    public void save() {
        //given
        when(favoriteService.save(any())).thenReturn(NEW_FAVORITE);

        //when
        Favorite favorite = favoriteService.save(TEST_STATION);

        //then
        assertThat(favorite.getStation().getName()).isEqualTo(STATION_NAME);
    }
}
