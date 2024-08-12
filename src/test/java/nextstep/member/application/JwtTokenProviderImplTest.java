package nextstep.member.application;

import nextstep.member.domain.LoginMember;
import nextstep.security.application.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertAll;

class JwtTokenProviderImplTest {

    private JwtTokenProvider<LoginMember> jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProviderImpl();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "test-atdd-secret-key");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 100000L);
    }

    @DisplayName("토큰 claims subject 정보에 id가 포함된다")
    @Test
    void parseTokenTest() {
        var id = 1L;
        var email = "email@email.com";
        var token = jwtTokenProvider.createToken(new LoginMember(id, email));
        var loginMember = jwtTokenProvider.parseToken(token);
        assertAll(() ->
                        Assertions.assertThat(loginMember.getId()).isEqualTo(id),
                () -> Assertions.assertThat(loginMember.getEmail()).isEqualTo(email)
        );
    }
}
