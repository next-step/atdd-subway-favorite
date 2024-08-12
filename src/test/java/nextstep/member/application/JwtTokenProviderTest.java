package nextstep.member.application;

import nextstep.security.service.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertAll;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "test-atdd-secret-key");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 100000L);
    }

    @DisplayName("토큰 claims subject 정보에 id가 포함된다")
    @Test
    void parseTokenTest() {
        var id = 1L;
        var email = "email@email.com";
        var token = jwtTokenProvider.createToken(1L, email);
        var loginMember = jwtTokenProvider.parseLoginMember(token);
        assertAll(() ->
                        Assertions.assertThat(loginMember.getId()).isEqualTo(id),
                () -> Assertions.assertThat(loginMember.getEmail()).isEqualTo(email)
        );
    }
}
