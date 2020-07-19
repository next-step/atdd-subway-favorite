package nextstep.subway.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인코딩/디코딩/변환과 관련된 ConvertUtils의 유닛 테스트")
class ConvertUtilsTest {

    @DisplayName("base64 디코딩 테스트")
    @Test
    void b64Decode() {

        // given
        final String message = "b64 encoded message";
        final byte[] encode = Base64.getEncoder().encode(message.getBytes(StandardCharsets.UTF_8));
        final String encodedMessage = new String(encode);

        // when
        final String decoded = ConvertUtils.b64Decode(encodedMessage);

        // then
        assertThat(decoded).isEqualTo(message);
    }

    @DisplayName("임의의 Object를 JSON string으로 잘 변환하는지 테스트")
    @Test
    void stringify() {

        // given
        final Dummy dummy = new Dummy("hyeyoom");

        // when
        final String jsonString = ConvertUtils.stringify(dummy);

        // then
        assertThat(jsonString).contains("hyeyoom");
        assertThat(jsonString).contains("name");
    }

    public static class Dummy {
        private final String name;

        public Dummy(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}