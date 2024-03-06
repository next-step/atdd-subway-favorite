package nextstep.auth.infra;

import nextstep.auth.domain.User;
import nextstep.auth.domain.UserGetter;
import org.springframework.web.client.RestTemplate;

public class UserGetterImpl implements UserGetter {


    @Override
    public User getUser() {
        RestTemplate restTemplate = new RestTemplate();
        return null;
    }
}
