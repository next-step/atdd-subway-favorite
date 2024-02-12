package nextstep.utils;

import nextstep.member.acceptance.AuthSteps;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {


    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private MemberRepository memberRepository;

    public String ACCESS_TOKEN;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        dataLoader.loadData();

        memberRepository.save(new Member("admin@gmail.com", "password", 20));
        ACCESS_TOKEN = AuthSteps.로그인_요청("admin@gmail.com", "password");
    }
}
