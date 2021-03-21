package nextstep.subway.auth.ui.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter{

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {

        TokenRequest tokenRequest = objectMapper.readValue(request.getReader(), TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }
}
