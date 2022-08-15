package nextstep.subway.unit;

import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class BearerTokenAuthenticationFilterTest {

    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    BearerTokenAuthenticationFilter filter;

    @Test
    void preHandle() {
        String token = "";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("Authorization", token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThat(filter.preHandle(request, response, null)).isTrue();
    }
}
