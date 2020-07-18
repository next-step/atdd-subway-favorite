package nextstep.subway.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("인증 정보 추출 학습 테스트")
class AuthorizationExtractorTest {

    private static final String EMAIL = "email@email.com";
    private static final String REGEX = ":";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("Basic방식으로 헤더에서 인증 정보를 추출한다")
    void extractBasicType() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        byte[] targetBytes = (EMAIL + REGEX + PASSWORD).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials);

        //when
        String extracted = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);

        //then
        assertThat(extracted).isEqualTo(credentials);
    }
}