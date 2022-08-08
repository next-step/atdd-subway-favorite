package nextstep.auth.authentication.nonchain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextMapper;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter extends AuthenticationNonChainFilter {
    @Qualifier("defaultAuthenticationProvider")
    private final AuthenticationProvider<AuthenticationToken> authenticationProvider;

    public AuthenticationToken convert(HttpServletRequest request) {
        String userEmail = request.getParameter("username");
        String password = request.getParameter("password");
        return new AuthenticationToken(userEmail, password);
    }

    @Override
    public Authentication authentication(AuthenticationToken authenticationToken) {
        return authenticationProvider.authenticate(authenticationToken);
    }

    @Override
    public void afterProcessing(Authentication authenticate, HttpServletResponse response) throws Exception {
        SecurityContextMapper.setContext(authenticate.getPrincipal().toString(), authenticate.getAuthorities());
    }
}
