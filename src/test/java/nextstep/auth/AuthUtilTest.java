package nextstep.auth;

import nextstep.util.AuthUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuthUtilTest {

    public static final String PREFIX = "Bearer ";
    public static final String VALID_HEADER = "Bearer gg";
    public static final String INVALID_HEADER = "dddd kk";

    @Test
    void 인증() {
        assertThat(AuthUtil.match(VALID_HEADER, PREFIX)).isTrue();
    }

    @Test
    void 인증_아님() {
        assertThat(AuthUtil.match(INVALID_HEADER, PREFIX)).isFalse();
    }

    @Test
    void 헤더파싱() {
        assertThat(AuthUtil.parseAccessToken(VALID_HEADER, PREFIX)).isEqualTo("gg");
    }

}