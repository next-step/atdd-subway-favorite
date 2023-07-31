package nextstep.utils;

import static nextstep.member.acceptance.MemberSteps.회원_로그인_요청_후_token_추출;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static String ACCESS_TOKEN = null;

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();
    }

    public static String getAccessToken() {

        if (ACCESS_TOKEN == null) {
            회원_생성_요청(EMAIL, PASSWORD, AGE);
            ACCESS_TOKEN = 회원_로그인_요청_후_token_추출(EMAIL, PASSWORD);
        }

        return ACCESS_TOKEN;
    }
}
