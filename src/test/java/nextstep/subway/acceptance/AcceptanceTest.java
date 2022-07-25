package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private MemberRepository memberRepository;

    protected static final String EMAIL = "admin@email.com";
    protected static final String PASSWORD = "password";
    protected static final Integer AGE = 20;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE, List.of(RoleType.ROLE_ADMIN.name())));
    }
}
