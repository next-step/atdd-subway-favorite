package nextstep;

import nextstep.utils.DataLoader;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    protected void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();
    }
}
