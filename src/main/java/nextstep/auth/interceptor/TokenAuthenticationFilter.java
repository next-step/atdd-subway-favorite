package nextstep.auth.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.BuildResponseTokenFailException;
import nextstep.auth.exception.OutputStreamException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationFilter extends AuthenticationNonChainingFilter {

    public TokenAuthenticationFilter(UserDetailsService userDetailsService,
                                     JwtTokenProvider jwtTokenProvider,
                                     ObjectMapper objectMapper)
    {
        super(userDetailsService, jwtTokenProvider, objectMapper);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));

        TokenRequest tokenRequest = objectMapper.readValue(content, TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    @Override
    protected void afterAuthentication(Authentication authentication, HttpServletResponse response) {
        String token = jwtTokenProvider.createToken(String.valueOf(authentication.getPrincipal()), authentication.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        try {
            String responseToClient = objectMapper.writeValueAsString(tokenResponse);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().print(responseToClient);
        } catch (JsonProcessingException e) {
            throw new BuildResponseTokenFailException();
        } catch (IOException e) {
            throw new OutputStreamException();
        }
    }

}
