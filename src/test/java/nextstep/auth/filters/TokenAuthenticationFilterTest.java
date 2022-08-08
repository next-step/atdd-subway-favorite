package nextstep.auth.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.filters.converter.TokenAuthenticationConverter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetails;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JwtTokenProvider.class, TokenAuthenticationConverter.class})
class TokenAuthenticationFilterTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private TokenAuthenticationConverter authenticationConverter;

    private TokenAuthenticationFilter authenticationFilter;

    @BeforeEach
    void setUp() {
        authenticationFilter = new TokenAuthenticationFilter(null, jwtTokenProvider, authenticationConverter);
    }

    @Test
    void convert() throws Exception {
        // given
        HttpServletRequest request = bearerAuthHttpRequest(EMAIL, PASSWORD);

        // when
        AuthenticationToken token = authenticationFilter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() throws IOException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        UserDetails user = new User(EMAIL, PASSWORD, List.of(RoleType.ROLE_MEMBER.name()));
        authenticationFilter.authenticate(user, response);

        // then
        String responseString = response.getContentAsString();
        TokenResponse tokenResponse = OBJECT_MAPPER.readValue(responseString, TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    private MockHttpServletRequest bearerAuthHttpRequest(String email, String password) throws JsonProcessingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(email, password);
        String payload = OBJECT_MAPPER.writeValueAsString(tokenRequest);
        request.setContent(payload.getBytes(StandardCharsets.UTF_8));
        return request;
    }
}
