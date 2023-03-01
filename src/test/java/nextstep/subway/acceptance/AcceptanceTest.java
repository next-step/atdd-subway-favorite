package nextstep.subway.acceptance;

import nextstep.subway.stub.GithubResponses;
import nextstep.subway.utils.DataLoader;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.acceptance.LoginSteps.깃헙_로그인_요청;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    protected DataLoader dataLoader;
    protected String 사용자_AccessToken;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        dataLoader.loadDataWithGithubUser();
        사용자_AccessToken = 깃헙_로그인_요청(GithubResponses.사용자1.getCode()).jsonPath().getString("accessToken");
    }
}
