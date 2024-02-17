package nextstep.api;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import nextstep.utils.data.DataLoader;
import nextstep.utils.data.DatabaseCleanupExecutor;

/**
 * @author : Rene Choi
 * @since : 2024/02/02
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Component
public class CommonAcceptanceTest {

	@Autowired
	private DatabaseCleanupExecutor databaseCleanupExecutor;
	@Autowired
	private DataLoader dataLoader;

	@BeforeEach
	public void databaseCleanup() {
		databaseCleanupExecutor.execute();
		dataLoader.loadData();
	}
}
