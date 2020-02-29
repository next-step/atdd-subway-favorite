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
        jdbcTemplate.update("ALTER TABLE STATION ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE LINE ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE EDGE ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE USER ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FAVORITE ALTER COLUMN id RESTART WITH 1");
    }
}

