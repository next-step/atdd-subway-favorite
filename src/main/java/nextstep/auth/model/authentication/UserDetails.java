package nextstep.auth.model.authentication;

public interface UserDetails {
    Long getId();

    String getUsername();

    String getCredential();

    boolean validateCredential(String targetPassword);
}
