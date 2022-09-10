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

    protected static final String ADMIN_EMAIL = "admin@email.com";
    protected static final String ADMIN_PASSWORD = "password";
    protected static final int ADMIN_AGE = 20;
    protected static final String MEMBER_EMAIL = "member@email.com";
    protected static final String MEMBER_PASSWORD = "password";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    protected String ADMIN_TOKEN;
    protected String MEMBER_TOKEN;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();
        ADMIN_TOKEN = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
        MEMBER_TOKEN = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);
    }
}
