package nextstep.member.application.dto;

public class TokenDto {
    private String accessToken;

    public TokenDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
