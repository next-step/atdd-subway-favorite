package nextstep.member.application;

public interface OAuth2Client {

    String requestAccessToken(String code);
}
