package nextstep.auth.domain;

public interface UserCreator {

    void createUser(String email, String password, int age);

}
