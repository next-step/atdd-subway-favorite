package nextstep.auth.application;


public interface UserDetailService {
    UserDetail loadUser(String userId);
    UserDetail saveUser(String userId, String password, int age);
}
