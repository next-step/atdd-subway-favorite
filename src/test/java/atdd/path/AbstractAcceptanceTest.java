package atdd.path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
public class AbstractAcceptanceTest extends SoftAssertionTest {
    @Autowired
    public WebTestClient webTestClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected void cleanAllDatabases() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "STATION", "LINE", "EDGE", "USER");
        jdbcTemplate.update("ALTER TABLE STATION ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE LINE ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE EDGE ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE USER ALTER COLUMN id RESTART WITH 1");
    }
}
