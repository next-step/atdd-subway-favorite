package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final String OTHER_EMAIL = "other@email.com";
    private static final String OTHER_PASSWORD = "password123";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;
    String 관리자;
    String 일반사용자;
    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자 = 로그인_되어_있음(EMAIL, PASSWORD);
        일반사용자 = 로그인_되어_있음(OTHER_EMAIL, OTHER_PASSWORD);
    }
}
