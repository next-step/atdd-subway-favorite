package nextstep.auth.oauth2;

public interface OAuth2UserService {
    OAuth2User loadUserByOAuth2User(OAuth2UserRequest oAuth2UserRequest);

}
