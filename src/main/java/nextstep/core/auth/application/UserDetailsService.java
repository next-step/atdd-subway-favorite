package nextstep.core.auth.application;

public interface UserDetailsService {

    boolean verifyUser(String email, String password);

    String findOrCreate(String email);
}
