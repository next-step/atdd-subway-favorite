package nextstep.auth.authentication;

import io.jsonwebtoken.lang.Assert;
import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.context.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

// ProividerManager는 for문을 통해 모든 provider를 조회하면서 authenticate 인증 처리를 한다\
public class ProviderManager implements AuthenticationManager {
    private List<AuthenticationProvider> providers = Collections.emptyList();
    private AuthenticationManager parent;


    public ProviderManager(List<AuthenticationProvider> providers) {
        Assert.notNull(providers, "providers list cannot be null");
        this.providers = providers;
    }

    public ProviderManager(List<AuthenticationProvider> providers, AuthenticationManager parent) {
        Assert.notNull(providers, "providers list cannot be null");
        this.providers = providers;
        this.parent = parent;
    }

    // 모든 provider 를 조회하면서 인증 처리를 함.
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = null;
        Authentication parentResult = null;

        for (AuthenticationProvider provider : getProviders()) {
            result = provider.authenticate(authentication);
            if (result != null) {
                break;
            }
        }

        // ProviderManager 에게 처리할수 있는 Provider 가 존재하지 않을경우 Parent 에게 위임함.
        if (result == null) {
            parentResult = this.parent.authenticate(authentication);
            result = parentResult;
        }

        return result;
    }

    public List<AuthenticationProvider> getProviders() {
        return this.providers;
    }
}
