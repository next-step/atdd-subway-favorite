package nextstep.member.domain;

import nextstep.DataLoader;
import nextstep.auth.domain.JwtAuthType;
import nextstep.member.application.JwtTokenProvider;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtAuthTypeTest extends AcceptanceTest {

    public static final String PREFIX = "Bearer ";
    public static String validHeader;

    @Autowired
    private JwtAuthType jwtAuthType;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void init() {
        String accessToken = jwtTokenProvider.createToken(DataLoader.EMAIL, List.of(RoleType.ROLE_MEMBER.name()));
        validHeader = PREFIX + accessToken;
    }

    @Test
    void jwt인증이다() {
        assertThat(jwtAuthType.match(PREFIX + "gg")).isTrue();
    }

    @Test
    void jwt인증이_아니다() {
        assertThat(jwtAuthType.match("test a")).isFalse();
    }

    @Test
    void 검증() {
        jwtAuthType.validate(validHeader);
    }

    @Test
    void 내정보_조회() {
        Member member = jwtAuthType.findMember(validHeader);
        assertThat(member).isNotNull();
    }
}