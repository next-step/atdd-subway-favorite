package nextstep.auth.application;

public interface UserDetailService {
    UserDetail findUser(String email);

    UserDetail findOrCreateUser(String email, Integer age);
}
