package nextstep.member;

import org.springframework.stereotype.Component;

@Component
public class OAuth2UserService {
    public OAuth2User loadUser(OAuth2UserRequest request) {

        return request.toOAuth2User();
    }
}
