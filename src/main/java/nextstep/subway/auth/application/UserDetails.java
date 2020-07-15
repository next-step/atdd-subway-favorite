package nextstep.subway.auth.application;

public interface UserDetails {
    Object getPrincipal();

    Object getCredentials();

    boolean checkCredentials(Object credentials);
}
