package nextstep.auth.context;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode
public class Authentication implements Serializable {
    private Object principal;
    private List<String> authorities;

    public Authentication(Object principal, List<String> authorities) {
        this.principal = principal;
        this.authorities = authorities;
    }

    public Object getPrincipal() {
        return principal;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
