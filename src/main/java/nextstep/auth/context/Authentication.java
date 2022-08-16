package nextstep.auth.context;

import java.io.Serializable;
import java.util.List;

// 현재 접근하는 주체의 정보와 권한을 담는 인터페이스.
public interface Authentication extends Serializable {
    List<String> getAuthorities();

    // Principal 객체를 가져옴
    Object getPrincipal();

    // 주로 비밀번호
    Object getCredentials();

    // 인증 여부를 가져옴
    boolean isAuthenticated();

    // 인증 여부를 설정함함
    void setAuthenticated(boolean isAuthenticated);
}
