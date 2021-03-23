package nextstep.subway.auth.infrastructure.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        TokenRequest tokenRequest;
        try {
            tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
        } catch (IOException e) {
            return null;
        }

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();
        return new AuthenticationToken(principal, credentials);
    }
}
