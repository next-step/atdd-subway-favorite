package nextstep.auth.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.exception.ResponseOutputStreamException;
import nextstep.auth.exception.TokenResponseParseException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.LoginMemberService;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationFilter extends AuthenticationNonChainingFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationFilter(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        super(loginMemberService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        try {
            String content = request.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            TokenRequest tokenRequest = objectMapper.readValue(content, TokenRequest.class);
            return createToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        } catch (IOException e) {
            throw new AuthenticationException();
        }
    }

    @Override
    protected void afterAuthentication(Authentication authentication, HttpServletResponse response) {
        String token = jwtTokenProvider.createToken(
                String.valueOf(authentication.getPrincipal()), authentication.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        try {
            String responseToClient = objectMapper.writeValueAsString(tokenResponse);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().print(responseToClient);
        } catch (JsonProcessingException e) {
            throw new TokenResponseParseException();
        } catch (IOException e) {
            throw new ResponseOutputStreamException();
        }
    }
}
