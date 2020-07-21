package nextstep.subway.auth.application.converter;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("토큰 기반 인증 컨버터 테스트")
class TokenAuthenticationConverterTest {

    private static final String NAME = "hyeyoom";
    private static final String PASS = "1234abcd";

    private MockHttpServletRequest request;
    private TokenAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        final String origin = NAME + ":" + PASS;
        final String credential = Base64.encode(origin.getBytes());

        request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + credential);

        converter = new TokenAuthenticationConverter();
    }

    @DisplayName("요청으로부터 Basic 인증을 잘 읽어서 토큰으로 만드는지 검사한다")
    @Test
    void convert() {

        // when
        final AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(NAME);
        assertThat(token.getCredentials()).isEqualTo(PASS);
    }
}