package nextstep.subway.unit;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static nextstep.utils.ObjectStringMapper.convertObjectAsString;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenProviderTest extends AcceptanceTest {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secretKey, 0);
    }

    @Test
    void authTokenIsExpired() {
        MemberResponse memberResponse = new MemberResponse(1L, "test@test.com", 10);
        String token = jwtTokenProvider.createToken(convertObjectAsString(memberResponse), List.of());

        assertThat(jwtTokenProvider.validateToken(token)).isFalse();
    }
}
