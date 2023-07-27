package nextstep.utils;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.auth.AuthSteps.회원_토큰_생성;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    private static final String EMAIL = "yuseongan@next.com";
    private static final String PASSWORD = "pass";

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;
    protected String 사용자1_토큰;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();

        회원_생성_요청(EMAIL, PASSWORD, 23);
        사용자1_토큰 = 회원_토큰_생성(EMAIL, PASSWORD);
    }
}
