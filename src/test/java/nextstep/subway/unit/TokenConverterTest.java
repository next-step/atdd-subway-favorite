package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.convertor.TokenConvertor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.unit.TokenAuthenticationInterceptorTest.createMockRequest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("토큰 컨버터 테스트")
public class TokenConverterTest {

    private TokenConvertor tokenConvertor = new TokenConvertor(new ObjectMapper());

    @Test
    void authenticate() throws IOException {
        //given
        MockHttpServletRequest mockRequest = createMockRequest();

        //when
        AuthenticationToken authenticationToken = tokenConvertor.convert(mockRequest);

        //then
        assertNotNull(authenticationToken);
    }

}
