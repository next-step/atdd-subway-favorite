package nextstep.subway.acceptance;

import nextstep.DataLoader;
import nextstep.subway.ApplicationContextTest;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class AcceptanceTest extends ApplicationContextTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();
    }
}
