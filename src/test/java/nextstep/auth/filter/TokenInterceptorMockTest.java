package nextstep.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenInterceptorMockTest {
    @Mock
    private LoginService loginService;
    @Autowired
    private AuthenticationFilter filter;
    private JwtTokenProvider provider = new JwtTokenProvider();

    private static final String userEmail = "admin@gmail.com";
    private static final String userPassword = "password";
    HttpServletRequest request;
    HttpServletResponse response;
    Object handler;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        // secret-key 설정
        FieldUtils.writeField(provider, "secretKey", "atdd-secret-key", true);
        filter = new TokenInterceptor(loginService, provider);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handler = mock(Object.class);
    }

    private MockHttpServletRequest createMockRequest(TokenRequest tokenRequest) throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        Base64 encoder = new Base64(0, new byte[0]);
        String userPass = "user1:password1";
        String encodedUserPass = encoder.encodeToString(userPass.getBytes());
        request.addHeader("Authorization", "BASIC " + encodedUserPass);
        return request;
    }

    @Test
    @DisplayName("사용자 principal을 이용해 사용자의 정보를 찾습니다.")
    void getEmailTest() throws IOException {
        // when
        request = createMockRequest(new TokenRequest(userEmail, userPassword));

        // then
        Authentication response = filter.getAuthentication(request);
        assertThat(response.getPrincipal()).isEqualTo(userEmail);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("사용자 principal을 이용해 사용자의 정보를 찾지 못하면 예외가 발생합니다.")
    void getEmailValidationTest(String email) throws IOException {
        request = createMockRequest(new TokenRequest(email, userPassword));

        assertThatThrownBy(
            () -> filter.getAuthentication(request)
        ).isInstanceOf(AuthenticationException.class);
    }


    @Test
    @DisplayName("사용자 credential이 일치하는지 확인합니다.")
    void getPasswordTest() throws IOException {
        // when
        request = createMockRequest(new TokenRequest(userEmail, userPassword));

        // then
        Authentication response = filter.getAuthentication(request);
        assertThat(response.getCredentials()).isEqualTo(userPassword);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("사용자 credential이 일치하는지 않으면 예외가 발생합니다.")
    void getPasswordValidationTest(String password) throws IOException {
        // when
        request = createMockRequest(new TokenRequest(userEmail, password));

        // then
        assertThatThrownBy(
            () -> filter.getAuthentication(request)
        ).isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("사용자 정보를 컨텍스트에 담고, Response Status OK 응답을 보냅니다.")
    void setResponseStatusTest() throws IOException {
        // when
        when(response.getOutputStream()).thenReturn(mock(ServletOutputStream.class));

        // then
        assertDoesNotThrow(
            () -> filter.execute(response, "admin@email.com", List.of(RoleType.ROLE_ADMIN.name()))
        );
    }

    @Test
    @DisplayName("request에서 사용자 정보를 가져와 컨텍스트에 저장합니다.")
    void preHandleTest() throws Exception {
        // given
        request = createMockRequest(new TokenRequest(userEmail, userPassword));

        // when
        when(loginService.isUserExist(any())).thenReturn(true);
        when(loginService.loadUserByUsername(any())).thenReturn(new LoginMember(userEmail, userPassword, List.of(RoleType.ROLE_ADMIN.name())));
        when(response.getOutputStream()).thenReturn(mock(ServletOutputStream.class));

        // then
        assertDoesNotThrow(
            () -> filter.preHandle(request, response, handler)
        );
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 예외가 발생한다.")
    void preHandleValidationTest1() throws IOException {
        // given
        request = createMockRequest(new TokenRequest(userEmail, userPassword));

        // when
        when(loginService.isUserExist(any())).thenReturn(false);

        assertThatThrownBy(
            () -> filter.preHandle(request, response, handler)
        ).isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다.")
    void preHandleValidationTest2() throws IOException {
        // given
        String password = "not Equals Password";
        request = createMockRequest(new TokenRequest(userEmail, userPassword));
        when(loginService.isUserExist(any())).thenReturn(true);

        // when
        when(loginService.loadUserByUsername(any())).thenReturn(new LoginMember(userEmail, password, List.of(RoleType.ROLE_ADMIN.name())));

        assertThatThrownBy(
            () -> filter.preHandle(request, response, handler)
        ).isInstanceOf(AuthenticationException.class);
    }

}
