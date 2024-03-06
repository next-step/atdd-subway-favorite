package nextstep.auth.infra;

import nextstep.auth.domain.UserCreator;
import nextstep.auth.infra.dto.UserRequest;
import org.springframework.web.client.RestTemplate;

public class UserCreatorImpl implements UserCreator {

    private final String url;

    public UserCreatorImpl(String url) {
        this.url = url;
    }

    @Override
    public void createUser(String email, String password, int age) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, new UserRequest(email, password, age), Object.class);
    }

}
