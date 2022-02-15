package nextstep.auth.model.authentication;

public interface UserDetails {
    String getUsername();

    String getCredential();

    boolean validateCredential(String targetPassword);
}
