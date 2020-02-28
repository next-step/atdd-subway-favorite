package atdd.path.dao;

import atdd.path.SoftAssertionTest;
import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import atdd.path.domain.User;
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
import static atdd.path.fixture.FavoriteFixture.STATION_TYPE;
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
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        favoriteDao = new FavoriteDao(jdbcTemplate);
        favoriteDao.setDataSource(dataSource);
        stationDao = new StationDao(jdbcTemplate);
        stationDao.setDataSource(dataSource);
        userDao = new UserDao(jdbcTemplate);
        userDao.setDataSource(dataSource);
    }

    @DisplayName("Favorite 에 Station 을 저장이 성공하는지")
    @Test
    public void save() {
        //given
        User user = userDao.save(NEW_USER);
        Station station = stationDao.save(TEST_STATION);

        //when
        Favorite favorite = favoriteDao.save(new Favorite(user, station), STATION_TYPE);

        //then
        assertThat(favorite.getId()).isNotNull();
        assertThat(favorite.getStation().getName()).isEqualTo(STATION_NAME);
        assertThat(favorite.getUser().getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("Id 로 Favorite 을 조회할 수 있는지")
    @Test
    public void findById() {
        //given
        User user = userDao.save(NEW_USER);
        Station station = stationDao.save(TEST_STATION);
        Favorite savedFavorite = favoriteDao.save(new Favorite(user, station), STATION_TYPE);

        //when
        Favorite favorite = favoriteDao.findById(savedFavorite.getId());

        //then
        assertThat(favorite.getId()).isNotNull();
        assertThat(favorite.getStation()).isEqualTo(savedFavorite.getStation());
    }

    @DisplayName("사용자 Id 로 등록된 Favorite 을 조회할 수 있는지")
    @Test
    public void findByUser() {
        //given
        User user = userDao.save(NEW_USER);
        Station firstStation = stationDao.save(TEST_STATION);
        Station secondStation = stationDao.save(TEST_STATION_15);
        favoriteDao.save(new Favorite(user, firstStation), STATION_TYPE);
        favoriteDao.save(new Favorite(user, secondStation), STATION_TYPE);

        //when
        List<Favorite> favorites = favoriteDao.findByUser(user);

        //then
        assertThat(favorites).hasSizeGreaterThan(1);
        assertThat(favorites.get(0).getStation().getName()).isEqualTo(STATION_NAME);
        assertThat(favorites.get(1).getStation().getName()).isEqualTo(STATION_NAME_15);
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


    @DisplayName("findById 로 나온 결과를 Favorite 로 만들어주는지")
    @Test
    public void mapFavorite(SoftAssertions softly) {
        //when
        Favorite favorite = favoriteDao.mapFavorite(getDaoFavorites());

        User user = favorite.getUser();
        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(favorite.getStation().getName()).isEqualTo(STATION_NAME);
        softly.assertThat(user.getName()).isEqualTo(KIM_NAME);
        softly.assertThat(user.getEmail()).isEqualTo(KIM_EMAIL);
    }

    @DisplayName("findByUser 로 나온 결과를 List<Favorite> 로 만들어주는지")
    @Test
    public void mapFavorites(SoftAssertions softly) {
        //when
        List<Favorite> favorites = favoriteDao.mapFavorites(getDaoFavorites(), NEW_USER);

        Station station = favorites.get(0).getStation();
        //then
        softly.assertThat(station.getId()).isNotNull();
        softly.assertThat(station.getName()).isEqualTo(STATION_NAME);
    }
}
