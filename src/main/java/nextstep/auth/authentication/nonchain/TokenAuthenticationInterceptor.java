package nextstep.auth.authentication.nonchain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TokenAuthenticationInterceptor extends AuthenticationNonChainFilter {
    private final JwtTokenProvider jwtTokenProvider;
    @Qualifier("defaultAuthenticationProvider")
    private final AuthenticationProvider<AuthenticationToken> authenticationProvider;

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    @Override
    public Authentication authentication(AuthenticationToken authenticationToken) {
        return authenticationProvider.authenticate(authenticationToken);
    }

    @Override
    public void afterProcessing(Authentication authenticate, HttpServletResponse response) throws IOException {
        String token = jwtTokenProvider.createToken(authenticate.getPrincipal().toString(), authenticate.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

}
