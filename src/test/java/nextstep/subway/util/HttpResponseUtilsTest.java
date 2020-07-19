package nextstep.subway.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("응답 관련된 간편한 처리를 위한 편의성 클래스 HttpResponseUtils 테스트")
class HttpResponseUtilsTest {

    @DisplayName("응답 바디를 작성하는 메서드 테스트")
    @Test
    void write() throws Exception {

        // given
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final String message = "hi";

        // when
        HttpResponseUtils.write(response, () -> message);

        // then
        assertThat(response.getContentAsString()).isEqualTo(message);
    }
}