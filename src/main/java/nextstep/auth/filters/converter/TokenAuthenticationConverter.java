package nextstep.auth.filters.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String content = request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = OBJECT_MAPPER.readValue(content, TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }
}
