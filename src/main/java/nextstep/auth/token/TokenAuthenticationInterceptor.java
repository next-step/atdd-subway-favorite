package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.authentication.filter.AbstractOneAuthenticationFilter;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.token.BearerAuthenticationToken;
import nextstep.auth.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends AbstractOneAuthenticationFilter {

    public TokenAuthenticationInterceptor(AuthenticationManager authenticationManager,
                                          AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(authenticationManager, authenticationSuccessHandler);
    }

    @Override
    protected Authentication convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new UsernamePasswordAuthenticationToken(principal, credentials);
    }
}
