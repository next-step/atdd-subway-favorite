package nextstep.subway.unit;

import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.subway.utils.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BearerTokenAuthenticationFilterMockTest {

    static String EMAIL = "admin@email.com";
    static String BEARER_TOKEN = SecurityUtil.getUnlimitedJwtTokenProvider().createToken(EMAIL, List.of());

    @Mock
    JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    BearerTokenAuthenticationFilter filter;

    @Test
    void preHandle() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + BEARER_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = new Authentication(EMAIL, List.of());

        when(jwtTokenProvider.toAuthentication(BEARER_TOKEN)).thenReturn(authentication);

        assertThat(filter.preHandle(request, response, null)).isTrue();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
    }
}
