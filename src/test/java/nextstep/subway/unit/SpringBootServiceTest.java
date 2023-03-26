package nextstep.subway.unit;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import nextstep.subway.utils.DatabaseCleanup;

@ActiveProfiles("test")
@SpringBootTest
public class SpringBootServiceTest {
	@Autowired
	private DatabaseCleanup databaseCleanup;

	@BeforeEach
	public void setUp() {
		databaseCleanup.execute();
	}
}
