package nextstep.auth.application;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TokenManager {
    private final List<TokenProvider> tokenProviders;

    public TokenManager(List<TokenProvider> tokenProviders) {
        this.tokenProviders = tokenProviders;
    }

    public String createToken(String principal,
                              TokenType tokenType) {
        TokenProvider tokenProvider = mayTokenProvider(tokenType).orElseThrow(() -> new IllegalArgumentException(
                "잘못된 토큰 발급 요청입니다."));
        return tokenProvider.createToken(principal);
    }

    public String getPrincipal(String token,
                               TokenType tokenType) {
        TokenProvider tokenProvider = mayTokenProvider(tokenType).orElseThrow(() -> new IllegalArgumentException(
                "잘못된 토큰 정보 조회 요청입니다."));
        return tokenProvider.getPrincipal(token);
    }

    private Optional<TokenProvider> mayTokenProvider(TokenType tokenType) {
        return tokenProviders.stream()
                .filter(provider -> provider.isSupport(tokenType))
                .findFirst();
    }
}
