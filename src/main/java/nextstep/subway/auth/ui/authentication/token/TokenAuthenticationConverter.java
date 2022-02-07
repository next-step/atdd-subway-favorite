package nextstep.subway.auth.ui.authentication.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.ui.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    private final ObjectMapper objectMapper;

    public TokenAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }
}
