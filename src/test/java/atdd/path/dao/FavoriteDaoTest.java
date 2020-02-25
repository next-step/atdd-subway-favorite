package atdd.path.dao;

import atdd.path.domain.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static atdd.path.TestConstant.TEST_STATION;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class FavoriteDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private FavoriteDao favoriteDao;

    @BeforeEach
    void setUp() {
        favoriteDao = new FavoriteDao(jdbcTemplate);
        favoriteDao.setDataSource(dataSource);
    }

    @Test
    public void save() {
        Favorite favorite = favoriteDao.save(new Favorite(TEST_STATION));

        assertThat(favorite.getId()).isNotNull();
        assertThat(favorite.getStation()).isEqualTo(TEST_STATION);
    }
}
