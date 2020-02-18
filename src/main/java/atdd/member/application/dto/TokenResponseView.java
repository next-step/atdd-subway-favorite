package atdd.member.application.dto;

public class TokenResponseView
{
    private String accessToken;
    private String type = "Bearer";

    public TokenResponseView() {}

    public TokenResponseView(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public static TokenResponseView of(String accessToken)
    {
        return new TokenResponseView(accessToken);
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getType()
    {
        return type;
    }
}
