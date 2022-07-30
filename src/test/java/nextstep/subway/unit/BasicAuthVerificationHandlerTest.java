package nextstep.subway.unit;

import nextstep.auth.context.Authentication;
import nextstep.auth.handler.BasicAuthVerificationHandler;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import nextstep.subway.fixture.MockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static nextstep.subway.fixture.MockMember.GUEST;
import static nextstep.subway.utils.UserUtils.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicAuthVerificationHandlerTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Basic 토큰을 전달하면 인증정보가 생성한다.")
    void createAuthenticationTest() {
        // given
        User user = createUser(GUEST);
        when(userDetailsService.loadUserByUsername(GUEST.getEmail())).thenReturn(user);
        BasicAuthVerificationHandler handler = new BasicAuthVerificationHandler(userDetailsService);

        // when
        Authentication authentication = handler.createAuthentication(createMockRequest(GUEST));

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(GUEST.getEmail());
        assertThat(authentication.getAuthorities()).isEqualTo(GUEST.getAuthorities());
    }

    private MockHttpServletRequest createMockRequest(MockMember member) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String authValue = String.join(":", member.getEmail(), member.getPassword());
        String encodingAuthValue = Base64.getEncoder().encodeToString(authValue.getBytes(StandardCharsets.UTF_8));
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic" + " " + encodingAuthValue);
        return request;
    }

}
