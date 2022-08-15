package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.UserDetailsService;
import nextstep.member.domain.LoginMember;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UsernamePasswordAuthenticationFilterMockTest {

    @Mock
    UserDetailsService userDetailsService;

    @InjectMocks
    UsernamePasswordAuthenticationFilter filter;

    @Test
    void preHandle() throws IOException {
        String email = "admin@email.com";
        String password = "password";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("email", email);
        request.setParameter("password", password);
        MockHttpServletResponse response = new MockHttpServletResponse();

        given(userDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(email, password, List.of()));

        assertThat(filter.preHandle(request, response, null)).isFalse();
    }

    @Test
    void convert() {
        String email = "admin@email.com";
        String password = "password";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("email", email);
        request.setParameter("password", password);

        assertThat(filter.convert(request)).isEqualTo(new AuthenticationToken(email, password));
    }

    @Test
    void authenticate() throws IOException {
        String email = "admin@email.com";
        String password = "password";
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.authenticate(new LoginMember(email, password, List.of()), response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(new Authentication(email, List.of()));
    }
}
