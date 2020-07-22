package nextstep.subway.auth.ui.interceptor.authentication;

public interface UserDetailsService {
    LoginMember loadUserByUsername(String principal);
}
