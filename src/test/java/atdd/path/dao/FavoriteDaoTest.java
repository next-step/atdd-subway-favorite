package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.*;
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
    private StationDao stationDao;
    private LineDao lineDao;
    private EdgeDao edgeDao;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        favoriteDao = new FavoriteDao(jdbcTemplate);
        favoriteDao.setDataSource(dataSource);

        stationDao = new StationDao(jdbcTemplate);
        stationDao.setDataSource(dataSource);

        lineDao = new LineDao(jdbcTemplate);
        lineDao.setDataSource(dataSource);

        edgeDao = new EdgeDao(jdbcTemplate);
        edgeDao.setDataSource(dataSource);

        memberDao = new MemberDao(jdbcTemplate);
        memberDao.setDataSource(dataSource);
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

    @DisplayName("경로를 즐겨찾기로 저장해야 한다")
    @Test
    void mustSaveFavoritePath() {
        Station savedStation = stationDao.save(TEST_STATION);
        Station savedStation2 = stationDao.save(TEST_STATION_2);
        Station savedStation3 = stationDao.save(TEST_STATION_3);
        Station savedStation4 = stationDao.save(TEST_STATION_4);

        Line savedLine = lineDao.save(TEST_LINE);
        int distance = 10;

        edgeDao.save(savedLine.getId(), Edge.of(savedStation, savedStation2, distance));
        edgeDao.save(savedLine.getId(), Edge.of(savedStation2, savedStation3, distance));
        edgeDao.save(savedLine.getId(), Edge.of(savedStation3, savedStation4, distance));

        Member savedMember = memberDao.save(TEST_MEMBER);

        FavoritePath favoritePath = new FavoritePath(savedMember, savedStation, savedStation4);
        FavoritePath savedFavoritePath = favoriteDao.saveForPath(favoritePath);
        Station sourceStation = savedFavoritePath.getSourceStation();
        Station targetStation = savedFavoritePath.getTargetStation();

        assertThat(sourceStation).isNotNull();
        assertThat(sourceStation.getName()).isEqualTo(STATION_NAME);
        assertThat(targetStation).isNotNull();
        assertThat(targetStation.getName()).isEqualTo(STATION_NAME_4);
    }

    @DisplayName("경로 즐겨찾기 목록을 조회해야 한다")
    @Test
    void mustFindFavoritePath() {
        Station savedStation = stationDao.save(TEST_STATION);
        Station savedStation2 = stationDao.save(TEST_STATION_2);
        Station savedStation3 = stationDao.save(TEST_STATION_3);
        Station savedStation4 = stationDao.save(TEST_STATION_4);

        Line savedLine = lineDao.save(TEST_LINE);
        int distance = 10;

        edgeDao.save(savedLine.getId(), Edge.of(savedStation, savedStation2, distance));
        edgeDao.save(savedLine.getId(), Edge.of(savedStation2, savedStation3, distance));
        edgeDao.save(savedLine.getId(), Edge.of(savedStation3, savedStation4, distance));

        Member savedMember = memberDao.save(TEST_MEMBER);

        favoriteDao.saveForPath(new FavoritePath(savedMember, savedStation, savedStation3));
        favoriteDao.saveForPath(new FavoritePath(savedMember, savedStation2, savedStation4));
        favoriteDao.saveForPath(new FavoritePath(savedMember, savedStation, savedStation4));

        List<FavoritePath> favorites = favoriteDao.findForPaths(savedMember);

        assertThat(favorites.size()).isEqualTo(3);
        assertThat(favorites).extracting("sourceStation.name")
                .containsExactly(STATION_NAME, STATION_NAME_2, STATION_NAME);

    }

}