package atdd.user.application.dto;

import atdd.user.jwt.JwtTokenProvider;

import static atdd.Constant.AUTH_SCHEME_BEARER;

public class LoginRequestView {
    private String email;
    private String password;
    private String accessToken;
    private String tokenType;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public LoginRequestView() {
    }

    public LoginRequestView(String email, String password, JwtTokenProvider jwtTokenProvider) {
        this.email = email;
        this.password = password;
        this.accessToken = jwtTokenProvider.createToken(email);
        this.tokenType = AUTH_SCHEME_BEARER;
    }
}
