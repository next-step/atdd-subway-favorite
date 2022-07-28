package nextstep.subway.acceptance;

import static java.util.Optional.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.utils.MockMember.ADMIN;
import static nextstep.subway.utils.MockMember.GUEST;

import io.restassured.RestAssured;
import java.util.Optional;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.utils.DatabaseCleanup;
import nextstep.subway.utils.MockMember;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeAll;
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
    public static String ADMIN_ACCESS_TOKEN;
    public static String GUEST_ACCESS_TOKEN;

    @Autowired
    private DatabaseCleanup databaseCleanup;


    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        memberRepository.saveAll(MockMember.getAllMembers());

        ADMIN_ACCESS_TOKEN = ofNullable(ADMIN_ACCESS_TOKEN).orElseGet(() -> 로그인_되어_있음(ADMIN));
        GUEST_ACCESS_TOKEN = ofNullable(GUEST_ACCESS_TOKEN).orElseGet(() -> 로그인_되어_있음(GUEST));
    }
}
