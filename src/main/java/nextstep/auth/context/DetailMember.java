package nextstep.auth.context;

public interface DetailMember {
    boolean checkPassword(String password);

    String getPassword();
}
