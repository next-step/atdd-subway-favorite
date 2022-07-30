package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.handler.UsernamePasswordAuthenticationHandler;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.subway.fixture.MockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.fixture.MockMember.GUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernamePasswordAuthenticationHandlerTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("사용자 EMAIL과 PASSWORD가 일치하면 사용자를 반환한다.")
    void preAuthenticationTest() throws IOException {
        // given
        User mockUser = createUser(GUEST);

        when(userDetailsService.loadUserByUsername(GUEST.getEmail())).thenReturn(mockUser);
        UsernamePasswordAuthenticationHandler handler = new UsernamePasswordAuthenticationHandler(userDetailsService);

        // when
        User user = handler.preAuthentication(createMockRequest(GUEST));

        // then
        assertThat(user.getPrincipal()).isEqualTo(GUEST.getEmail());
        assertThat(user.getAuthorities()).isEqualTo(GUEST.getAuthorities());
    }


    @Test
    @DisplayName("사용자 EMAIL이 없으면 인증이 실패한다.")
    void preAuthenticationEmailFailTest() {
        // when
        when(userDetailsService.loadUserByUsername(GUEST.getEmail())).thenReturn(null);

        UsernamePasswordAuthenticationHandler handler = new UsernamePasswordAuthenticationHandler(userDetailsService);

        // then
        assertThatThrownBy(() -> handler.preAuthentication(createMockRequest(GUEST)))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("사용자 PASSWORD가 다르면 인증이 실패한다.")
    void preAuthenticationPasswordFailTest() {
        // when
        User user = createUserWithPassword(GUEST, "other_password");
        when(userDetailsService.loadUserByUsername(GUEST.getEmail())).thenReturn(user);

        UsernamePasswordAuthenticationHandler handler = new UsernamePasswordAuthenticationHandler(userDetailsService);

        // then
        assertThatThrownBy(() -> handler.preAuthentication(createMockRequest(GUEST)))
                .isInstanceOf(AuthenticationException.class);

    }

    @Test
    @DisplayName("SecurityContextHolder에 유저 정보가 저장된다.")
    void afterAuthenticationTest() {
        // given
        User user = createUser(GUEST);

        // when
        UsernamePasswordAuthenticationHandler handler = new UsernamePasswordAuthenticationHandler(userDetailsService);
        handler.afterAuthentication(user, new MockHttpServletResponse());

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getPrincipal()).isEqualTo(user.getPrincipal());
        assertThat(authentication.getAuthorities()).isEqualTo(user.getAuthorities());

    }

    private User createUser(MockMember member) {
        return new LoginMember(member.getEmail(), member.getPassword(), member.getAuthorities());
    }

    private User createUserWithPassword(MockMember member, String password) {
        return new LoginMember(member.getEmail(), password, member.getAuthorities());
    }

    private MockHttpServletRequest createMockRequest(MockMember member) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", member.getEmail());
        request.addParameter("password", member.getPassword());
        return request;
    }


}
