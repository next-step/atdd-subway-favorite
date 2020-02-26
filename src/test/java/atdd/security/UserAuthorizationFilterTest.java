package atdd.security;

import atdd.user.service.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class UserAuthorizationFilterTest {

    private UserAuthorizationFilter userAuthorizationFilter;

    private AuthorizationService authorizationService = mock(AuthorizationService.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    private final String authorization = "authorization!!!!!";

    @BeforeEach
    void setup() {
        request.addHeader(HttpHeaders.AUTHORIZATION, authorization);
        this.userAuthorizationFilter = new UserAuthorizationFilter(authorizationService, objectMapper);
    }

    @Test
    void doFilterInternal() throws Exception {
        final FilterChain filterChain = mock(FilterChain.class);
        given(authorizationService.isAuthorized(eq(authorization), any(Date.class))).willReturn(true);

        userAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @DisplayName("doFilterInternal - 인증되지 않은 사용자는 401 코드와 에러 메시지 반환")
    @Test
    void doFilterInternalNotAuthorized() throws Exception {
        final FilterChain filterChain = mock(FilterChain.class);
        final String expectedErrorBody = "{\"code\":401,\"message\":\"인증되지 않은 사용자 입니다.\"}";
        given(authorizationService.isAuthorized(eq(authorization), any(Date.class))).willReturn(false);


        userAuthorizationFilter.doFilterInternal(request, response, filterChain);


        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedErrorBody);

        verify(filterChain, times(0)).doFilter(any(), any());
    }

}