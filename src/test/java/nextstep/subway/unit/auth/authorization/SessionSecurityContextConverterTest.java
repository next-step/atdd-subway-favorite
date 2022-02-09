package nextstep.subway.unit.auth.authorization;

import static nextstep.auth.context.SecurityContextHolder.*;
import static org.assertj.core.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import nextstep.auth.authorization.converter.SessionSecurityContextConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;

@SuppressWarnings("ConstantConditions")
class SessionSecurityContextConverterTest {
    private static final String PRINCIPAL = "안녕";
    private SessionSecurityContextConverter sessionSecurityContextConverter;

    @BeforeEach
    void setUp() {
        this.sessionSecurityContextConverter = new SessionSecurityContextConverter();
    }

    private MockHttpServletRequest createMockHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Authentication authentication = new Authentication(PRINCIPAL);
        SecurityContext securityContext = new SecurityContext(authentication);
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return request;
    }

    @Test
    void convert() {
        // given
        MockHttpServletRequest request = createMockHttpServletRequest();

        // when
        SecurityContext securityContext = sessionSecurityContextConverter.convert(request);

        // then
        assertThat(securityContext.getAuthentication().getPrincipal()).isEqualTo(PRINCIPAL);
    }
}
