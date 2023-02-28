package nextstep.member.domain;

import nextstep.DataLoader;
import nextstep.auth.domain.GithubAuthType;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GithubAuthTypeTest extends AcceptanceTest {

    public static final String PREFIX = "token ";
    public static final String VALID_HEADER = PREFIX + DataLoader.ACCESS_TOKEN;
    public static final String INVALID_HEADER = "no gg";

    @Autowired
    private GithubAuthType githubAuthType;

    @Test
    void 깃허브_인증() {
        assertThat(githubAuthType.match(VALID_HEADER)).isTrue();
    }

    @Test
    void 깃허브_인증_아님() {
        assertThat(githubAuthType.match(INVALID_HEADER)).isFalse();
    }

    @Test
    void 검증() {
        githubAuthType.validate(VALID_HEADER);
    }

    @Test
    void 내정보_조회() {
        Member member = githubAuthType.findMember(VALID_HEADER);
        assertThat(member).isNotNull();
    }
}
