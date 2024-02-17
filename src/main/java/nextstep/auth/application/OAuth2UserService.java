package nextstep.auth.application;

import nextstep.auth.domain.OAuth2User;
import nextstep.auth.domain.OAuth2UserRequest;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserService {
    public OAuth2User loadUser(OAuth2UserRequest request) {

        return request.toOAuth2User();
    }
}
