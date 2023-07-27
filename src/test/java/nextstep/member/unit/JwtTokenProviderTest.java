package nextstep.member.unit;

import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.RoleType;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class JwtTokenProviderTest extends AcceptanceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("유효한 토큰이 요청 왔을때 검증 성공")
    void validateTokenTrue() {
        // Given
        String token = jwtTokenProvider.createToken("email@email.com", RoleType.ROLE_MEMBER.name());

        // When
        boolean actual = jwtTokenProvider.validateToken(token);

        // Then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 토큰이 요청 왔을때 검증 실패")
    void validateTokenFalse() {
        // Given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String token = "ABC";

        // When
        boolean actual = jwtTokenProvider.validateToken(token);

        // Then
        Assertions.assertThat(actual).isFalse();
    }
}
