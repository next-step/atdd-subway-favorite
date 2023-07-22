package nextstep.api.auth.application.token.oauth2;

public interface OAuth2UserService {
    OAuth2User loadUser(final OAuth2UserRequest oAuth2UserRequest);
}
