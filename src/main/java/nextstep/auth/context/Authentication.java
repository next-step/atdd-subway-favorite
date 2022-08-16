package nextstep.auth.context;

import java.io.Serializable;
import java.util.List;

public interface Authentication extends Serializable {
    List<String> getAuthorities();

    Object getPrincipal();

    // 주로 비밀번호
    Object getCredentials();

    // 인증 여부를 가져옴
    boolean isAuthenticated();

    // 인증 여부를 설정함함
    void setAuthenticated(boolean isAuthenticated);
}
