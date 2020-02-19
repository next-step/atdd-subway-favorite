package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Member;
import atdd.path.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
class FavoriteDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private FavoriteDao favoriteDao;
    private MemberDao memberDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        favoriteDao = new FavoriteDao(jdbcTemplate);
        favoriteDao.setDataSource(dataSource);

        memberDao = new MemberDao(jdbcTemplate);
        memberDao.setDataSource(dataSource);

        stationDao = new StationDao(jdbcTemplate);
        stationDao.setDataSource(dataSource);
    }

    @DisplayName("지하철역 즐겨찾기 등록해야 한다")
    @Test
    public void mustSaveForStation() {
        Member savedMember = memberDao.save(TEST_MEMBER);
        Station savedStation = stationDao.save(TEST_STATION);

        FavoriteStation savedFavoriteStation = favoriteDao.saveForStation(new FavoriteStation(savedMember, savedStation));
        Station station = savedFavoriteStation.getStation();

        assertThat(savedFavoriteStation).isNotNull();
        assertThat(station).isNotNull();
        assertThat(station.getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("지하철역 즐겨찾기 목록을 조회해야 한다")
    @Test
    public void mustFindForStation() {
        Member savedMember = memberDao.save(TEST_MEMBER);
        Station savedStation = stationDao.save(TEST_STATION);
        Station savedStation2 = stationDao.save(TEST_STATION_2);
        Station savedStation3 = stationDao.save(TEST_STATION_3);
        favoriteDao.saveForStation(new FavoriteStation(savedMember, savedStation));
        favoriteDao.saveForStation(new FavoriteStation(savedMember, savedStation2));
        favoriteDao.saveForStation(new FavoriteStation(savedMember, savedStation3));

        final List<FavoriteStation> favorites = favoriteDao.findForStations(savedMember);

        assertThat(favorites.size()).isEqualTo(3);
        assertThat(favorites).extracting("station.name")
                .containsExactly(STATION_NAME, STATION_NAME_2, STATION_NAME_3);
    }

    @DisplayName("즐겨찾기 아이디로 즐겨찾기 한 지하철역을 삭제해야 한다")
    @Test
    void mustDeleteById() {
        Member savedMember = memberDao.save(TEST_MEMBER);
        Station savedStation = stationDao.save(TEST_STATION);
        FavoriteStation savedFavorite = favoriteDao.saveForStation(new FavoriteStation(savedMember, savedStation));

        favoriteDao.deleteForStationById(savedFavorite.getId());

        assertThrows(
                NoDataException.class,
                () -> favoriteDao.deleteForStationById(savedFavorite.getId())
        );
    }

}