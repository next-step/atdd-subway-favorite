package nextstep.member.auth;

public interface OAuth2Client {

    String getAccessToken(String code);

    OAuth2User loadUser(String accessToken);
}
