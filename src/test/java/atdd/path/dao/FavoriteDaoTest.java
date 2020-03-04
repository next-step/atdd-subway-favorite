package atdd.path.dao;

import atdd.path.DatabaseConfigTest;
import atdd.path.domain.Edge;
import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static atdd.path.TestConstant.*;
import static atdd.path.dao.FavoriteDao.EDGE_TYPE;
import static atdd.path.dao.FavoriteDao.STATION_TYPE;
import static atdd.path.fixture.FavoriteFixture.*;
import static atdd.path.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteDaoTest extends DatabaseConfigTest {
    public static final long DEFAULT_ID = 1L;

    @Autowired
    private DataSource dataSource;

    private FavoriteDao favoriteDao;

    @Sql("/clean-all.sql")
    @BeforeEach
    void setUp() {
        favoriteDao = new FavoriteDao(jdbcTemplate);
        favoriteDao.setDataSource(dataSource);
    }

    @Sql("/create-station-favorites.sql")
    @DisplayName("Favorite 에 Station 을 저장이 성공하는지")
    @Test
    public void save(SoftAssertions softly) {
        //when
        Favorite favorite = favoriteDao.save(new Favorite(NEW_USER, TEST_STATION), STATION_TYPE);

        Station favoriteStation = (Station) favorite.getItem();

        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(favoriteStation.getName()).isEqualTo(STATION_NAME);
        softly.assertThat(favorite.getUser().getName()).isEqualTo(KIM_NAME);
    }

    @Sql("/create-station-favorites.sql")
    @DisplayName("즐겨찾기를 지하철로 등록한 것의 Id 로 Favorite 을 조회할 수 있는지")
    @Test
    public void findStationById(SoftAssertions softly) {
        Favorite favorite = favoriteDao.findStationById(DEFAULT_ID, STATION_TYPE);

        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(favorite.getItem()).isEqualTo(NEW_STATION_FAVORITE.getItem());
    }

    @Sql("/create-edge-favorite.sql")
    @DisplayName("즐겨찾기를 지하철경로로 등록한 것의 Id 로 Favorite 을 조회할 수 있는지")
    @Test
    public void findEdgeById(SoftAssertions softly) {
        //when
        Favorite favorite = favoriteDao.findEdgeById(DEFAULT_ID, EDGE_TYPE);

        Edge resultEdge = (Edge) favorite.getItem();

        //then
        softly.assertThat(favorite.getId()).isNotNull();
        softly.assertThat(favorite.getItem().getId()).isNotNull();
        softly.assertThat(resultEdge.getSourceStation().getName()).isEqualTo(TEST_STATION.getName());
        softly.assertThat(resultEdge.getTargetStation().getName()).isEqualTo(TEST_STATION_2.getName());
    }


    @Sql("/create-station-favorites.sql")
    @DisplayName("사용자 Id 로 등록된 지하철역 Favorite 을 조회할 수 있는지")
    @Test
    public void findStationByUser(SoftAssertions softly) {
        //when
        List<Favorite> favorites = favoriteDao.findStationByUser(NEW_USER);

        Station favoriteStation = (Station) favorites.get(0).getItem();
        Station secondFavoriteStation = (Station) favorites.get(1).getItem();

        //then
        softly.assertThat(favorites).hasSizeGreaterThan(1);
        softly.assertThat(favoriteStation.getName()).isEqualTo(STATION_NAME);
        softly.assertThat(secondFavoriteStation.getName()).isEqualTo(STATION_NAME_2);
    }

    @Sql("/create-edge-favorite.sql")
    @DisplayName("사용자 Id 로 등록된 지하철 경로 Favorite 을 조회할 수 있는지")
    @Test
    public void findEdgeByUser(SoftAssertions softly) {
        //when
        List<Favorite> favorites = favoriteDao.findEdgeByUser(NEW_USER);

        Edge savedEdge = (Edge) favorites.get(0).getItem();

        //then
        softly.assertThat(savedEdge.getSourceStation().getName()).isEqualTo(TEST_STATION.getName());
        softly.assertThat(savedEdge.getTargetStation().getName()).isEqualTo(TEST_STATION_2.getName());
    }

    @Sql("/create-station-favorites.sql")
    @DisplayName("사용자가 등록된 지하철역 즐겨찾기를 삭제 가능한지")
    @Test
    public void deleteStation() {
        //when
        favoriteDao.deleteItem(NEW_USER, STATION_ID);
        List<Favorite> favorites = favoriteDao.findStationByUser(NEW_USER);

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
        List<Favorite> favorites = favoriteDao.mapStationFavorites(getDaoFavorites(), NEW_USER);

        Station station = (Station) favorites.get(0).getItem();

        //then
        softly.assertThat(station.getId()).isNotNull();
        softly.assertThat(station.getName()).isEqualTo(STATION_NAME);
    }
}
