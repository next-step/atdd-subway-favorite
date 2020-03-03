package atdd.path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;



@JdbcTest
public class DatabaseConfigTest extends SoftAssertionTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void cleanAllDatabases() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "STATION", "LINE", "EDGE", "USER", "FAVORITE");
        jdbcTemplate.update("ALTER TABLE STATION AUTO_INCREMENT = 1");
        jdbcTemplate.update("ALTER TABLE LINE AUTO_INCREMENT = 1");
        jdbcTemplate.update("ALTER TABLE EDGE AUTO_INCREMENT = 1");
        jdbcTemplate.update("ALTER TABLE USER AUTO_INCREMENT = 1");
        jdbcTemplate.update("ALTER TABLE FAVORITE AUTO_INCREMENT = 1");
    }
}

