package nextstep.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionAdviceTest {

    private ExceptionAdvice exceptionAdvice;

    @BeforeEach
    void setUp() {
        exceptionAdvice = new ExceptionAdvice();
    }

    @DisplayName("BaseException에 @ResponseStatus 가 붙어있는 경우 value에 해당하는 Response Code를 반환한다")
    @Test
    void responseStatusTest() {
        ResponseEntity<Map<String, String>> response = exceptionAdvice.baseException(new TestException("권한 없음"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    static class TestException extends BaseException {
        public TestException(final String message) {
            super(message);
        }
    }

}
