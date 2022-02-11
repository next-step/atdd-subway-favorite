package nextstep.auth.authentication.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthenticationTokenConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException  {
        TokenRequest tokenRequest = readJson(request);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    private TokenRequest readJson(HttpServletRequest request) throws IOException {
        String formData = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        return new ObjectMapper().readValue(formData, TokenRequest.class);
    }
}
