package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.member.User;
import nextstep.auth.member.UserDetailService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernamePasswordFilterMockTest {
    @Mock
    private UserDetailService userDetailService;
    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
    private AuthenticationFilter filter;
    private static final String userEmail = "admin@gmail.com";
    private static final String userPassword = "password";
    public static final User USER = User.of(userEmail, userPassword, List.of(RoleType.ROLE_ADMIN.name()));
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    HttpServletRequest request;
    HttpServletResponse response;
    Object handler;

    @BeforeEach
    void setUp() {
        usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        filter = new AuthenticationFilter(usernamePasswordAuthenticationFilter, userDetailService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handler = mock(Object.class);
    }

    @Test
    @DisplayName("사용자 principal을 이용해 사용자의 정보를 찾습니다.")
    void getEmailTest() {
        // when
        getRequestParameterStubbing(USERNAME, userEmail);
        getRequestParameterStubbing(PASSWORD, userPassword);

        // then
        Authentication response = usernamePasswordAuthenticationFilter.getAuthentication(request);
        assertThat(response.getPrincipal()).isEqualTo(userEmail);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("사용자 principal을 이용해 사용자의 정보를 찾지 못하면 예외가 발생합니다.")
    void getEmailValidationTest(String email) {
        getRequestParameterStubbing(USERNAME, email);

        assertThatThrownBy(
            () -> usernamePasswordAuthenticationFilter.getAuthentication(request)
        ).isInstanceOf(AuthenticationException.class);
    }


    @Test
    @DisplayName("사용자 credential이 일치하는지 확인합니다.")
    void getPasswordTest() {
        // when
        getRequestParameterStubbing(USERNAME, userEmail);
        getRequestParameterStubbing(PASSWORD, userPassword);

        // then
        Authentication response = usernamePasswordAuthenticationFilter.getAuthentication(request);
        assertThat(response.getCredentials()).isEqualTo(userPassword);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("사용자 credential이 일치하는지 않으면 예외가 발생합니다.")
    void getPasswordValidationTest(String password) {
        // when
        getRequestParameterStubbing(USERNAME, userEmail);
        getRequestParameterStubbing(PASSWORD, password);

        // then
        assertThatThrownBy(
            () -> usernamePasswordAuthenticationFilter.getAuthentication(request)
        ).isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("사용자 정보를 컨텍스트에 담고, Response Status OK 응답을 보냅니다.")
    void setResponseStatusTest() {
        // when
        setResponseStatus();

        // then
        assertDoesNotThrow(
            () -> usernamePasswordAuthenticationFilter.responseOk(response, "admin@email.com", List.of(RoleType.ROLE_ADMIN.name()))
        );
    }

    @Test
    @DisplayName("request에서 사용자 정보를 가져와 컨텍스트에 저장합니다.")
    void preHandleTest() throws Exception {
        // given
        getRequestParameterStubbing(USERNAME, userEmail);
        getRequestParameterStubbing(PASSWORD, userPassword);

        // when
        when(userDetailService.loadUserByUsername(any())).thenReturn(USER);
        setResponseStatus();

        // then
        assertDoesNotThrow(
            () -> filter.preHandle(request, response, handler)
        );
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 예외가 발생한다.")
    void preHandleValidationTest1() {
        // given
        getRequestParameterStubbing(USERNAME, userEmail);
        getRequestParameterStubbing(PASSWORD, userPassword);

        // when & then
        assertThatThrownBy(
            () -> filter.preHandle(request, response, handler)
        ).isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다.")
    void preHandleValidationTest2() {
        // given
        String password = "not equals password";
        getRequestParameterStubbing(USERNAME, userEmail);
        getRequestParameterStubbing(PASSWORD, password);

        // when
        when(userDetailService.loadUserByUsername(any())).thenReturn(USER);

        assertThatThrownBy(
            () -> filter.preHandle(request, response, handler)
        ).isInstanceOf(AuthenticationException.class);
    }

    private void setResponseStatus() {
        doNothing().when(response).setStatus(HttpStatus.OK.value());
    }

    private OngoingStubbing<String> getRequestParameterStubbing(String parma, String get) {
        return when(request.getParameter(parma)).thenReturn(get);
    }
}
