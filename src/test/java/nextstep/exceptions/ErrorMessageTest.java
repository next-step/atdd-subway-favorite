package nextstep.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorMessageTest {

    @DisplayName("포맷팅된 문자를 반환한다")
    @Test
    void formatTest() {
        var message = ErrorMessage.NON_EXISTENT_STATION.getFormattingMessage(1L);
        assertThat(message).isEqualTo( "1 : 존재하지 않는 역입니다.");
    }

}
