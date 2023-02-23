package nextstep.member.auth;

public interface Auth2Client {

    String getAccessToken(String code);

    OAuth2User loadUser(String accessToken);
}
