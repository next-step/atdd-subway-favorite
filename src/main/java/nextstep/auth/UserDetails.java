package nextstep.auth;


import java.util.List;

public interface UserDetails {

    String getPrincipal();
    List<String> getAuthorities();

    boolean checkPassword(String password);

}
