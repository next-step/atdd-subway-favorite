package nextstep.auth.unit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
class TokenAuthenticationInterceptorTest extends AuthTest {
    private static final String ACCESS_TOKEN = "accessToken";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationConverter tokenAuthenticationConverter;

    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @Test
    void preHandle() throws IOException {
        // given
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider,
                tokenAuthenticationConverter, objectMapper);
        HttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        tokenAuthenticationInterceptor.preHandle(request, response, new Object());

        // then
        TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>(){
        };
        Map<String, String> tokenMap = objectMapper.readValue(response.getContentAsString(), typeRef);
        String accessToken = tokenMap.get(ACCESS_TOKEN);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(jwtTokenProvider.validateToken(accessToken)).isTrue();
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(objectMapper.writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
