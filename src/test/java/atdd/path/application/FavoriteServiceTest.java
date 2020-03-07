package atdd.path.application;

import atdd.path.application.dto.FavoritePathResponseView;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.dao.FavoritePathDao;
import atdd.path.dao.FavoriteStationDao;
import atdd.path.dao.StationDao;
import atdd.path.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = FavoriteService.class)
public class FavoriteServiceTest {
    @Autowired
    private FavoriteService favoriteService;

    @MockBean
    private FavoriteStationDao favoriteStationDao;

    @MockBean
    private FavoritePathDao favoritePathDao;

    @MockBean
    private StationDao stationDao;

    @MockBean
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(favoriteStationDao, favoritePathDao, stationDao, userDao);
    }

    @Test
    @DisplayName("지하철 즐겨찾기 등록")
    public void saveFavoriteStation() {
        given(stationDao.save(any())).willReturn(TEST_STATION_2);
        given(stationDao.findById(anyLong())).willReturn(TEST_STATION_2);
        given(favoriteStationDao.saveFavoriteStation(anyLong(), any())).willReturn(TEST_FAVORITE_STATION_2);

        FavoriteStationResponseView response = favoriteService.saveFavoriteStation(TEST_STATION_2.getId(), TEST_USER_2);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(TEST_FAVORITE_STATION_2.getId());
        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getId()).isEqualTo(TEST_USER.getId());
        assertThat(response.getUser().getEmail()).isEqualTo(TEST_USER.getEmail());
        assertThat(response.getStation()).isNotNull();
        assertThat(response.getStation().getId()).isEqualTo(TEST_STATION_2.getId());
        assertThat(response.getStation().getName()).isEqualTo(TEST_STATION_2.getName());
    }

    @Test
    @DisplayName("지하철 즐겨찾기 목록 조회")
    public void findFavoriteStations() {
        given(userDao.findByEmail(anyString())).willReturn(TEST_USER);
        given(stationDao.findById(TEST_STATION.getId())).willReturn(TEST_STATION);
        given(stationDao.findById(TEST_STATION_2.getId())).willReturn(TEST_STATION_2);
        given(stationDao.findById(TEST_STATION_3.getId())).willReturn(TEST_STATION_3);

        given(favoriteStationDao.saveFavoriteStation(TEST_STATION.getId(), TEST_USER)).willReturn(TEST_FAVORITE_STATION_1);
        given(favoriteStationDao.saveFavoriteStation(TEST_STATION_2.getId(), TEST_USER)).willReturn(TEST_FAVORITE_STATION_2);

        given(favoriteStationDao.findFavoriteStationsByUserId(TEST_USER.getId()))
                .willReturn(Arrays.asList(TEST_FAVORITE_STATION_1, TEST_FAVORITE_STATION_2));

        List<FavoriteStationResponseView> response = favoriteService.findFavoriteStations(TEST_USER);

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getUser().getEmail()).isEqualTo(TEST_USER.getEmail());
        assertThat(response.get(0).getStation().getName()).isEqualTo(TEST_STATION.getName());
        assertThat(response.get(1).getStation().getName()).isEqualTo(TEST_STATION_2.getName());
    }

    @Test
    @DisplayName("지하철 즐겨찾기 삭제")
    public void deleteFavoriteStation() {
        given(userDao.findByEmail(anyString())).willReturn(TEST_USER);
        given(favoriteStationDao.findFavoriteStationByIdAndUserId(TEST_STATION.getId(), TEST_USER.getId()))
                .willReturn(TEST_FAVORITE_STATION_1);
        given(favoriteStationDao.findFavoriteStationByIdAndUserId(TEST_STATION_2.getId(), TEST_USER.getId()))
                .willReturn(TEST_FAVORITE_STATION_2);

        favoriteService.deleteFavoriteStation(TEST_FAVORITE_STATION_1.getId(), TEST_USER);
        favoriteService.deleteFavoriteStation(TEST_FAVORITE_STATION_2.getId(), TEST_USER);
    }

    @Test
    @DisplayName("지하철 경로 즐겨찾기 등록")
    public void saveFavoritePath() {
        given(userDao.findByEmail(any())).willReturn(TEST_USER);
        given(favoritePathDao.save(TEST_USER.getId(), STATION_ID, STATION_ID_2)).willReturn(TEST_FAVORITE_PATH_1);

        FavoritePathResponseView response = favoriteService.saveFavoritePath(STATION_ID, STATION_ID_2, TEST_USER);

        assertThat(response.getPath()).isNotNull();
        assertThat(response.getPath().getStartStationId()).isEqualTo(1L);
        assertThat(response.getPath().getEndStationId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("지하철 경로 즐겨찾기 목록 조회")
    public void findFavoritePaths() {
        given(userDao.findByEmail(any())).willReturn(TEST_USER);
        given(stationDao.findById(TEST_STATION.getId())).willReturn(TEST_STATION);
        given(stationDao.findById(TEST_STATION_2.getId())).willReturn(TEST_STATION_2);
        given(favoritePathDao.findFavoritePathsByUserId(TEST_USER.getId()))
                .willReturn(Arrays.asList(TEST_FAVORITE_PATH_1, TEST_FAVORITE_PATH_2));

        List<FavoritePathResponseView> favoritePaths = favoriteService.findFavoritePaths(TEST_USER);

        assertThat(favoritePaths.size()).isEqualTo(2);
        assertThat(favoritePaths.get(0).getPath().getStartStationId()).isEqualTo(TEST_STATION.getId());
        assertThat(favoritePaths.get(1).getPath().getEndStationId()).isEqualTo(TEST_STATION_3.getId());
    }

    @Test
    @DisplayName("지하철 경로 즐겨찾기 삭제")
    public void deleteFavoritePath() {
        given(userDao.findByEmail(any())).willReturn(TEST_USER);
        given(favoritePathDao.findFavoritePathByIdAndUserId(TEST_FAVORITE_STATION_2.getId(), TEST_USER.getId()))
                .willReturn(TEST_FAVORITE_PATH_2);

        favoriteService.deleteFavoritePath(TEST_FAVORITE_STATION_2.getId(), TEST_USER);
    }
}
