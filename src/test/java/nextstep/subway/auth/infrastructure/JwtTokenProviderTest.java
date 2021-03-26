package nextstep.subway.auth.infrastructure;


import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @ParameterizedTest
    @ValueSource(strings = {"test", "test1", "test2"})
    public void createTokenTest(String payloadParam) {
        //Given
        String token = jwtTokenProvider.createToken(payloadParam);

        //When
        String payload = jwtTokenProvider.getPayload(token);

        //Then
        assertThat(payload).isEqualTo(payloadParam);
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "test1", "test2"})
    public void validateTest(String payloadParam) {
        //Given
        String token = jwtTokenProvider.createToken(payloadParam);

        //When
        boolean expected = jwtTokenProvider.validateToken(token);

        //Then
        assertThat(expected).isTrue();
    }
}
