package nextstep.auth.application;

import nextstep.auth.client.ExternalTokenFetcher;
import nextstep.auth.client.github.GithubTokenFetcher;
import org.springframework.stereotype.Component;

@Component
public class GithubTokenProvider implements TokenProvider {
    private final ExternalTokenFetcher githubTokenFetcher;

    public GithubTokenProvider(GithubTokenFetcher githubTokenFetcher) {
        this.githubTokenFetcher = githubTokenFetcher;
    }

    @Override
    public boolean isSupport(TokenType tokenType) {
        return TokenType.GITHUB == tokenType;
    }

    @Override
    public String createToken(String principal) {
        return githubTokenFetcher.requestToken(principal);
    }

    @Override
    public String getPrincipal(String token) {
        return githubTokenFetcher.findUser(token).getEmail();
    }

    @Override
    public boolean validateToken(String token) {
        // empty
        return false;
    }
}
