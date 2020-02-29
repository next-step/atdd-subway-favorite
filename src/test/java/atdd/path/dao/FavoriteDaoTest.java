package atdd.path.dao;

import atdd.path.SoftAssertionTest;
import atdd.path.domain.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static atdd.path.TestConstant.*;
import static atdd.path.dao.FavoriteDao.EDGE_TYPE;
import static atdd.path.dao.FavoriteDao.STATION_TYPE;
import static atdd.path.fixture.FavoriteFixture.getDaoEdgeFavorites;
import static atdd.path.fixture.FavoriteFixture.getDaoFavorites;
import static atdd.path.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class FavoriteDaoTest extends SoftAssertionTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private FavoriteDao favoriteDao;
    private StationDao stationDao;
    private LineDao lineDao;
    private EdgeDao edgeDao;
    private UserDao userDao;

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
        userDao = new UserDao(jdbcTemplate);
        userDao.setDataSource(dataSource);
    }

    @DisplayName("Favorite 에 Station 을 저장이 성공하는지")
    @Test
    public void save(SoftAssertions softly) {
        //given
        User user = userDao.save(NEW_USER);
        Station station = stationDao.save(TEST_STATION);

        //when
        Favorite favorite = favoriteDao.save(new Favorite(user, station), STATION_TYPE);

        Station favoriteStation = (Station) favorite.getItem();

        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(favoriteStation.getName()).isEqualTo(STATION_NAME);
        softly.assertThat(favorite.getUser().getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("즐겨찾기를 지하철로 등록한 것의 Id 로 Favorite 을 조회할 수 있는지")
    @Test
    public void findStationById(SoftAssertions softly) {
        //given
        User user = userDao.save(NEW_USER);
        Station station = stationDao.save(TEST_STATION);
        Favorite savedFavorite = favoriteDao.save(new Favorite(user, station), STATION_TYPE);

        //when
        Favorite favorite = favoriteDao.findStationById(savedFavorite.getId(), STATION_TYPE);

        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(favorite.getItem()).isEqualTo(savedFavorite.getItem());
    }

    @DisplayName("즐겨찾기를 지하철경로로 등록한 것의 Id 로 Favorite 을 조회할 수 있는지")
    @Test
    public void findEdgeById(SoftAssertions softly) {
        //given
        User user = userDao.save(NEW_USER);
        Station sourceStation = stationDao.save(TEST_STATION);
        Station targetStation = stationDao.save(TEST_STATION_2);
        Line line = lineDao.save(TEST_LINE);
        Edge edge = edgeDao.save(line.getId(), TEST_EDGE);

        Favorite savedFavorite = favoriteDao.save(new Favorite(user, edge), EDGE_TYPE);

        //when
        Favorite favorite = favoriteDao.findEdgeById(savedFavorite.getId(), EDGE_TYPE);

        Edge resultEdge = (Edge) favorite.getItem();

        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(favorite.getItem().getId()).isNotNull();
        softly.assertThat(resultEdge.getSourceStation().getName()).isEqualTo(sourceStation.getName());
        softly.assertThat(resultEdge.getTargetStation().getName()).isEqualTo(targetStation.getName());

    }


    @DisplayName("사용자 Id 로 등록된 Favorite 을 조회할 수 있는지")
    @Test
    public void findByUser(SoftAssertions softly) {
        //given
        User user = userDao.save(NEW_USER);
        Station firstStation = stationDao.save(TEST_STATION);
        Station secondStation = stationDao.save(TEST_STATION_15);
        favoriteDao.save(new Favorite(user, firstStation), STATION_TYPE);
        favoriteDao.save(new Favorite(user, secondStation), STATION_TYPE);

        //when
        List<Favorite> favorites = favoriteDao.findByUser(user);

        Station favoriteStation = (Station) favorites.get(0).getItem();
        Station secondFavoriteStation = (Station) favorites.get(1).getItem();

        //then
        softly.assertThat(favorites).hasSizeGreaterThan(1);
        softly.assertThat(favoriteStation.getName()).isEqualTo(STATION_NAME);
        softly.assertThat(secondFavoriteStation.getName()).isEqualTo(STATION_NAME_15);
    }

    @DisplayName("사용자가 등록된 지하철역 즐겨찾기를 삭제 가능한지")
    @Test
    public void deleteStation() {
        //given
        User user = userDao.save(NEW_USER);
        Station firstStation = stationDao.save(TEST_STATION);
        Station secondStation = stationDao.save(TEST_STATION_15);
        favoriteDao.save(new Favorite(user, firstStation), STATION_TYPE);
        favoriteDao.save(new Favorite(user, secondStation), STATION_TYPE);

        //when
        favoriteDao.deleteStation(user, STATION_ID);
        List<Favorite> favorites = favoriteDao.findByUser(user);

        //then
        assertThat(favorites).hasSize(1);
    }


    @DisplayName("findStationById 로 나온 결과를 Favorite 로 만들어주는지")
    @Test
    public void mapStationFavorite(SoftAssertions softly) {
        //when
        Favorite favorite = favoriteDao.mapStationFavorite(getDaoFavorites());

        User user = favorite.getUser();
        Station station = (Station) favorite.getItem();
        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(station.getName()).isEqualTo(STATION_NAME);
        softly.assertThat(user.getName()).isEqualTo(KIM_NAME);
        softly.assertThat(user.getEmail()).isEqualTo(KIM_EMAIL);
    }

    @DisplayName("findEdgeById 로 나온 결과를 Favorite 로 만들어주는지")
    @Test
    public void mapEdgeFavorite(SoftAssertions softly) {
        //when
        Favorite favorite = favoriteDao.mapEdgeFavorite(getDaoEdgeFavorites());

        User user = favorite.getUser();
        Edge edge = (Edge) favorite.getItem();

        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(edge.getSourceStation().getName()).isEqualTo(STATION_NAME);
        softly.assertThat(user.getName()).isEqualTo(KIM_NAME);
        softly.assertThat(user.getEmail()).isEqualTo(KIM_EMAIL);
    }


    @DisplayName("findByUser 로 나온 결과를 List<Favorite> 로 만들어주는지")
    @Test
    public void mapFavorites(SoftAssertions softly) {
        //when
        List<Favorite> favorites = favoriteDao.mapFavorites(getDaoFavorites(), NEW_USER);

        Station station = (Station) favorites.get(0).getItem();

        //then
        softly.assertThat(station.getId()).isNotNull();
        softly.assertThat(station.getName()).isEqualTo(STATION_NAME);
    }
}
