package nextstep.member.unit.token;

import nextstep.auth.token.service.JwtTokenProvider;
import nextstep.member.constants.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static nextstep.member.fixture.MemberFixture.회원_정보;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Test
    @DisplayName("올바른 토큰을 전달 받았을 때 검증에 성공한다.")
    void successValidateToken() {
        // given
        String token = tokenProvider.createToken(회원_정보.getEmail(), RoleType.ROLE_MEMBER.name());

        // when
        boolean isValid = tokenProvider.validateToken(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("올바르지 않은 토큰을 전달 받았을 때 검증에 실패한다.")
    void failValidateToken() {
        // given
        String token = "not_valid_token";

        // when
        boolean isValid = tokenProvider.validateToken(token);

        // then
        assertThat(isValid).isFalse();
    }

}
