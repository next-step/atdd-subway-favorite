package atdd.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LoginUserInfoMethodArgumentResolverTest {

    private LoginUserInfoMethodArgumentResolver loginUserInfoMethodArgumentResolver;

    private LoginUserRegistry loginUserRegistry = mock(LoginUserRegistry.class);

    @BeforeEach
    void setup() {
        loginUserInfoMethodArgumentResolver = new LoginUserInfoMethodArgumentResolver(loginUserRegistry);
    }

    @DisplayName("supportsParameter - 파라미터의 타입이 LoginUser 이면 true")
    @Test
    void supportsParameter() throws Exception {
        final MethodParameter methodParameter = new FakeMethodParameter(LoginUserInfo.class);


        final boolean supports = loginUserInfoMethodArgumentResolver.supportsParameter(methodParameter);


        assertThat(supports).isTrue();
    }

    @DisplayName("supportsParameter - 파라미터의 타입이 LoginUser 가 아니면 false")
    @Test
    void supportsParameterNotLoginUser() throws Exception {
        final MethodParameter methodParameter = new FakeMethodParameter(MethodParameter.class);


        final boolean supports = loginUserInfoMethodArgumentResolver.supportsParameter(methodParameter);


        assertThat(supports).isFalse();
    }

    @DisplayName("resolveArgument - loginUserRegistry 가 반환한 LoginUserInfo 를 반환한다.")
    @Test
    void resolveArgument() throws Exception {
        final LoginUserInfo loginUserInfo = LoginUserInfo.of(11L, "email!!", "name!!", "password!!");
        given(loginUserRegistry.getCurrentLoginUser()).willReturn(loginUserInfo);

        final Object result = loginUserInfoMethodArgumentResolver.resolveArgument(null, null, null, null);

        assertThat(result).isEqualTo(loginUserInfo);
    }

    private static class FakeMethodParameter extends MethodParameter {

        private final Class<?> parameterType;

        public FakeMethodParameter(Class<?> parameterType) {
            super(mock(MethodParameter.class));
            this.parameterType = parameterType;
        }

        @Override
        public Class<?> getParameterType() {
            return this.parameterType;
        }

    }

}