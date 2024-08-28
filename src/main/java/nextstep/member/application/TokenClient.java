package nextstep.member.application;


import nextstep.member.application.dto.OAuthProfileResponse;

public interface TokenClient {
    String requestAccessToken(String code);
    OAuthProfileResponse requestUserProfile(String accessToken);
}
