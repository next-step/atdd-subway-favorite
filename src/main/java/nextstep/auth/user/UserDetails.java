package nextstep.auth.user;

import java.util.List;

public interface UserDetails {
    public boolean isValidPassword(String password);

    public String getEmail();

    public String getPassword();

    public List<String> getAuthorities();
}
