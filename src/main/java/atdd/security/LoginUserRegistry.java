package atdd.security;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoginUserRegistry {

    private final ThreadLocal<LoginUserInfo> currentLoginUser = new ThreadLocal<>();

    public void setCurrentLoginUser(LoginUserInfo loginUser) {
        currentLoginUser.set(loginUser);
    }

    public LoginUserInfo getCurrentLoginUser() {
        final LoginUserInfo loginUser = currentLoginUser.get();
        if (Objects.isNull(loginUser)) {
            throw new IllegalStateException("로그인한 사용자가 없습니다.");
        }

        return loginUser;
    }

}
