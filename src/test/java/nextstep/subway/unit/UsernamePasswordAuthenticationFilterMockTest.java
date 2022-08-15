package nextstep.subway.unit;

import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UsernamePasswordAuthenticationFilterMockTest {

    @Mock
    LoginMemberService loginMemberService;

    @InjectMocks
    UsernamePasswordAuthenticationFilter filter;

    @Test
    void preHandle() {
        String email = "admin@email.com";
        String password = "password";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("email", email);
        request.setParameter("password", password);
        MockHttpServletResponse response = new MockHttpServletResponse();

        given(loginMemberService.loadUserByUsername(any())).willReturn(new LoginMember(email, password, List.of()));

        assertThat(filter.preHandle(request, response, null)).isTrue();
    }
}
