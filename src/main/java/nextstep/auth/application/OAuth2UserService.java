package nextstep.auth.application;

import nextstep.auth.application.dto.OAuth2UserRequest;
import nextstep.auth.domain.OAuth2User;

public interface OAuth2UserService {
  OAuth2User loadUser(OAuth2UserRequest userRequest);
}
