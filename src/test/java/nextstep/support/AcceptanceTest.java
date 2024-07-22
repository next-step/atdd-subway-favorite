package nextstep.support;

import static nextstep.Fixtures.aMember;
import static nextstep.member.acceptance.steps.AuthAcceptanceSteps.로그인_요청;

import io.restassured.RestAssured;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
  @LocalServerPort private int port;

  @Autowired private DatabaseCleanup databaseCleanup;
  @Autowired private DataLoader dataLoader;

  protected String accessToken;

  @BeforeEach
  protected void setUp() {
    if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
      RestAssured.port = port;
      databaseCleanup.afterPropertiesSet();
    }
    databaseCleanup.execute();
    dataLoader.loadData();

    Member member = aMember().build();
    accessToken = 로그인_요청(member.getEmail(), member.getPassword());
  }
}
