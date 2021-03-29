package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.ui.AuthenticationConverter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter {

    public static final String EMAIL_FIELD = "email";
    public static final String PASSWORD_FIELD = "password";
    private final ObjectMapper mapper;

    public TokenAuthenticationConverter() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String body = CharStreams.toString(request.getReader());
        Map<String, String> map = mapper.readValue(body, Map.class);

        String principal = map.get(EMAIL_FIELD);
        String credentials = map.get(PASSWORD_FIELD);

        return new AuthenticationToken(principal, credentials);
    }
}
