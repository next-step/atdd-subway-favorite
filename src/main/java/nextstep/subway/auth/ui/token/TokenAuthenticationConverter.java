package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.ui.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter {
    private ObjectMapper objectMapper;

    public TokenAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        try {
            TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
            String principal = tokenRequest.getEmail();
            String credentials = tokenRequest.getPassword();

            return new AuthenticationToken(principal, credentials);
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
}
