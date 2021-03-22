package nextstep.subway.auth.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.base.AuthenticationConverter;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(),TokenRequest.class);

        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }
}