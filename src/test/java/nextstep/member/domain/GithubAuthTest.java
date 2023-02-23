package nextstep.member.domain;

import nextstep.DataLoader;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GithubAuthTest extends AcceptanceTest {

    public static final String PREFIX = "token ";
    public static final String VALID_HEADER = PREFIX + DataLoader.ACCESS_TOKEN;
    public static final String INVALID_HEADER = "no gg";

    @Autowired
    private GithubAuth githubAuth;

    @Test
    void 깃허브_인증() {
        assertThat(githubAuth.match(VALID_HEADER)).isTrue();
    }

    @Test
    void 깃허브_인증_아님() {
        assertThat(githubAuth.match(INVALID_HEADER)).isFalse();
    }

    @Test
    void 검증() {
        githubAuth.validate(VALID_HEADER);
    }

    @Test
    void 내정보_조회() {
        Member member = githubAuth.findMember(VALID_HEADER);
        assertThat(member).isNotNull();
    }
}
