package nextstep.auth.authentication;

public interface LoginMember {
    boolean checkPassword(String credentials);

    Long getId();
}
