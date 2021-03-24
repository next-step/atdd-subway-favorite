package nextstep.subway.auth.ui.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken converter(HttpServletRequest request) {
        try {
            TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
            return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
