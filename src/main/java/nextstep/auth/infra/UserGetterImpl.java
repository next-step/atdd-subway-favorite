package nextstep.auth.infra;

import java.util.Map;
import nextstep.auth.domain.User;
import nextstep.auth.domain.UserGetter;
import nextstep.auth.domain.exception.UserException;
import nextstep.auth.infra.dto.UserResponse;
import nextstep.exception.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class UserGetterImpl implements UserGetter {

    private String url;

    public UserGetterImpl(String url) {
        this.url = url;
    }

    @Override
    public User getUser(String email) throws UserException.NotFoundUserException {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        String url = this.url + "?email={email}";
        Map<String, String> params = Map.of("email", email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        UserResponse response;
        try {
            response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity,
                    UserResponse.class,
                    params)
                .getBody();
        } catch (Exception e) {
            throw new UserException.NotFoundUserException();
        }

        return new User(response.getId(), response.getEmail(), response.getAge());
    }

    @Override
    public User getUser(String email, String password) {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        String url = this.url + "?email={email}&password={password}";
        Map<String, String> params = Map.of("email", email, "password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        UserResponse response;
        try {
            response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity,
                    UserResponse.class,
                    params)
                .getBody();
        } catch (Exception e) {
            throw new BadRequestException(BadRequestException.MEMBER_NOT_FOUND);
        }
        return new User(response.getId(), response.getEmail(), response.getAge());
    }

}
