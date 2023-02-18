package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import nextstep.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;

public abstract class AcceptanceTest extends ApplicationContextTest {

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
