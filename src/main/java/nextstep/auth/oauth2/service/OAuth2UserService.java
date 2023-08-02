package nextstep.auth.oauth2.service;

import nextstep.auth.oauth2.dto.OAuth2UserRequest;
import nextstep.auth.oauth2.dto.OAuth2User;

public interface OAuth2UserService {

    OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest);

}
