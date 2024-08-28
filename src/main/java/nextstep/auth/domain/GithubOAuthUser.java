package nextstep.auth.domain;

public class GithubOAuthUser implements OAuthUser {
    private String email;

    public GithubOAuthUser(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return this.email;
    }
}
