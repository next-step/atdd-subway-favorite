package atdd.path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;



@JdbcTest
public class DatabaseConfigTest extends SoftAssertionTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
}

