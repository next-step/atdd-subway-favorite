package atdd.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginUserInfoRegistryTest {

    final LoginUserInfo loginUser = LoginUserInfo.of(111L, "email@email.com", "name!!!", "password!!!");
    private LoginUserRegistry loginUserRegistry;

    @BeforeEach
    void setup() {
        this.loginUserRegistry = new LoginUserRegistry();
    }

    @Test
    void setLoginUser() throws Exception {

        loginUserRegistry.setCurrentLoginUser(loginUser);

        final LoginUserInfo currentLoginUser = loginUserRegistry.getCurrentLoginUser();

        assertThat(currentLoginUser).isEqualTo(loginUser);
    }

    @DisplayName("getLoginUser - 저장된 LoginUser 가 없으면 에러")
    @Test
    void getLoginUserNotSetLoginUser() throws Exception {
        assertThatThrownBy(() -> loginUserRegistry.getCurrentLoginUser())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("로그인한 사용자가 없습니다.");
    }

    @DisplayName("getLoginUser - 다른 스레드에서 접근하면 로그인 사용자가 존재하지 않는다.")
    @Test
    void getLoginUserOtherThread() throws Exception {
        loginUserRegistry.setCurrentLoginUser(loginUser);

        final CompletableFuture<LoginUserInfo> completableFuture = CompletableFuture.supplyAsync(() -> loginUserRegistry.getCurrentLoginUser());

        assertThatThrownBy(completableFuture::get)
                .isInstanceOf(ExecutionException.class)
                .hasCause(new IllegalStateException("로그인한 사용자가 없습니다."));
    }

}