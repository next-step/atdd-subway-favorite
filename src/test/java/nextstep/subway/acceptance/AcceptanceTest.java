package nextstep.subway.acceptance;

import nextstep.DataLoader;
import nextstep.subway.ApplicationContextTest;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

public class AcceptanceTest extends ApplicationContextTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;
    protected String 관리자_토큰;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();

        관리자_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }
}
