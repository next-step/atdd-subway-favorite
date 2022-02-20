package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.convertor.SessionConvertor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.subway.acceptance.MemberSteps.PASSWORD_FIELD;
import static nextstep.subway.acceptance.MemberSteps.USERNAME_FIELD;
import static nextstep.subway.unit.TokenAuthenticationInterceptorTest.EMAIL;
import static nextstep.subway.unit.TokenAuthenticationInterceptorTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("세션 컨버터 테스트")
public class SessionConvertorTest {

    private SessionConvertor sessionConvertor = new SessionConvertor();

    @Test
    void authenticate() throws IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(USERNAME_FIELD, EMAIL);
        request.setParameter(PASSWORD_FIELD, PASSWORD);

        //when
        AuthenticationToken authenticationToken = sessionConvertor.convert(request);

        //then
        assertNotNull(authenticationToken);
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }
}
