package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static nextstep.subway.acceptance.auth.AuthSteps.ADMIN_EMAIL;
import static nextstep.subway.acceptance.auth.AuthSteps.ADMIN_PASSWORD;
import static nextstep.subway.acceptance.member.MemberSteps.로그인_되어_있음;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    public static String adminAccessToken;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute("member", "member_role");
        adminAccessToken = Optional.ofNullable(adminAccessToken)
                .orElseGet(() -> 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD));
    }
}
