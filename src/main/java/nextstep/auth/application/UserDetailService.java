package nextstep.auth.application;


public interface UserDetailService {
    UserDetail loadUser(String userId);

}
