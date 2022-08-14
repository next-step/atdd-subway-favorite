package nextstep.auth.userdetail;

public interface UserDetailService {

    UserDetails loadUserByUsername(String username);

}
