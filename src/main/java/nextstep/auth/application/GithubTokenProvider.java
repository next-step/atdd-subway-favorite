package nextstep.auth.application;

import nextstep.auth.client.ExternalTokenFetcher;

public class GithubTokenProvider implements TokenProvider {
    private final ExternalTokenFetcher externalTokenFetcher;

    public GithubTokenProvider(ExternalTokenFetcher externalTokenFetcher) {
        this.externalTokenFetcher = externalTokenFetcher;
    }

    @Override
    public String createToken(String principal) {
        return externalTokenFetcher.requestToken(principal);
    }

    @Override
    public String getPrincipal(String token) {
        return externalTokenFetcher.findUser(token).getEmail();
    }

    @Override
    public boolean validateToken(String token) {
        // empty
        return false;
    }
}
