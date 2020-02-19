package atdd.path.application;

import atdd.path.dao.FavoriteDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.FavoritePath;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = FavoriteService.class)
class FavoriteServiceTest {

    @MockBean
    private FavoriteDao favoriteDao;

    @MockBean
    private StationDao stationDao;

    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(favoriteDao, stationDao);
    }

    @DisplayName("지하철역 즐겨찾기 등록을 해야한다")
    @Test
    public void mustSaveForStation() {
        given(stationDao.findById(anyLong())).willReturn(TEST_STATION);
        given(favoriteDao.saveForStation(any())).willReturn(TEST_FAVORITE_STATION);

        FavoriteStation favoriteStation = favoriteService.saveForStation(TEST_MEMBER, STATION_ID);
        Station station = favoriteStation.getStation();

        assertThat(favoriteStation).isNotNull();
        assertThat(station).isNotNull();
        assertThat(station.getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("경로 즐겨찾기 등록을 해야한다")
    @Test
    public void mustSaveForPath() {
        given(stationDao.findById(STATION_ID)).willReturn(TEST_STATION);
        given(stationDao.findById(STATION_ID_4)).willReturn(TEST_STATION_4);
        given(favoriteDao.saveForPath(any())).willReturn(TEST_FAVORITE_PATH);

        FavoritePath favoritePath = favoriteService.saveForPath(TEST_MEMBER, STATION_ID, STATION_ID_4);
        Station sourceStation = favoritePath.getSourceStation();
        Station targetStation = favoritePath.getTargetStation();

        assertThat(sourceStation).isNotNull();
        assertThat(sourceStation.getName()).isEqualTo(STATION_NAME);
        assertThat(targetStation).isNotNull();
        assertThat(targetStation.getName()).isEqualTo(STATION_NAME_4);
    }

}