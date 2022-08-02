package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import nextstep.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
	@LocalServerPort
	int port;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	@Autowired
	private DataLoader dataLoader;

	private String AdminAccessToken;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		databaseCleanup.execute();
		dataLoader.loadData();
		AdminAccessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
	}

	public String getAdminAccessToken() {
		return this.AdminAccessToken;
	}
}
