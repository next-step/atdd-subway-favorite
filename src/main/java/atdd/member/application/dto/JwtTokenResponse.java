package atdd.member.application.dto;

public class JwtTokenResponse {

    private final static String TYPE = "bearere";
    private String token;

    public JwtTokenResponse() {
    }

    public JwtTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return TYPE;
    }
}
