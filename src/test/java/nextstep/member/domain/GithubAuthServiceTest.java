package nextstep.member.domain;

import nextstep.DataLoader;
import nextstep.auth.domain.GithubAuthService;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GithubAuthServiceTest extends AcceptanceTest {

    public static final String PREFIX = "token ";
    public static final String VALID_HEADER = PREFIX + DataLoader.ACCESS_TOKEN;

    @Autowired
    private GithubAuthService githubAuthType;

    @Test
    void 검증() {
        githubAuthType.validate(VALID_HEADER);
    }

    @Test
    void 내정보_조회() {
        assertThat(githubAuthType.findMember(VALID_HEADER)).isNotNull();
    }
}
