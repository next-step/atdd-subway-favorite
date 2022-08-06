package nextstep.auth.member;

public interface UserDetailService {
    UserDetails loadUserByUsername(String username);

    boolean isUserExist(String username);
}
