package nextstep.member.infra;

import java.util.Map;
import nextstep.member.application.UserAuthenticator;
import nextstep.member.infra.dto.AuthResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class UserAuthenticatorImpl implements UserAuthenticator {

    private final String tokenUrl;

    public UserAuthenticatorImpl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    @Override
    public String authenticate(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = tokenUrl + "?accessToken={accessToken}";
        Map<String, String> params = Map.of("accessToken", accessToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        AuthResponse authResponse = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
            AuthResponse.class, params).getBody();

        return authResponse.getEmail();
    }
}
