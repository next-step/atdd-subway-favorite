package atdd.path.application;

import atdd.path.SoftAssertionTest;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.fixture.FavoriteFixture.NEW_FAVORITE;
import static atdd.path.fixture.FavoriteFixture.NEW_FAVORITE_CREATE_VIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FavoriteServiceTest extends SoftAssertionTest {
    private FavoriteService favoriteService;
    @MockBean
    private FavoriteDao favoriteDao;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(favoriteDao);
    }

    @DisplayName("사용자가 지하철 등록에 성공하는지")
    @Test
    public void save() {
        //given
        when(favoriteDao.save(any())).thenReturn(NEW_FAVORITE);

        //when
        Favorite favorite = favoriteService.save(NEW_FAVORITE_CREATE_VIEW);

        //then
        assertThat(favorite.getStation().getName()).isEqualTo(STATION_NAME);
    }
}
