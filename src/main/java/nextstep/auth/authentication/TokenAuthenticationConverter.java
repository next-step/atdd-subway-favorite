package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.token.TokenRequest;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest servletRequest) {
        // TODO: request에서 AuthenticationToken 객체 생성하기
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String request = servletRequest.getReader()
                .lines()
                .collect(Collectors.joining());

            TokenRequest tokenRequest = objectMapper.readValue(request, TokenRequest.class);

            return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        } catch (IOException e) {
            return new AuthenticationToken();
        }
    }
}
