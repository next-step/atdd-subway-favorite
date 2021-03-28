package nextstep.subway.auth.ui.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter {
    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);

        String principal = tokenRequest.getEmail(); // 본인
        String credentials = tokenRequest.getPassword(); // 자격

        return new AuthenticationToken(principal, credentials);
    }
}
