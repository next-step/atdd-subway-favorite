package nextstep.auth.domain;

public interface UserGetter {

    User getUser(String email);

    User getUser(String email, String password);

}
