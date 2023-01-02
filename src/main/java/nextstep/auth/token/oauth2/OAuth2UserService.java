package nextstep.auth.token.oauth2;

public interface OAuth2UserService {
    OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest);
}
