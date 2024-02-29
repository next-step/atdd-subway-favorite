package nextstep.core.auth.application;

public interface UserDetailsService {

    boolean validateUser(String email, String password);

    String findOrCreate(String email);
}
