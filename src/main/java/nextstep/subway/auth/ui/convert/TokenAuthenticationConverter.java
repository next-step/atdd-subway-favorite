package nextstep.subway.auth.ui.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {

        TokenRequest tokenRequest = objectMapper.readValue(request.getReader(), TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }
}
