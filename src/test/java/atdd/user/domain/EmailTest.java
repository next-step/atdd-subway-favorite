package atdd.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "email@email.com",
            "email@email.co.kr"
    })
    void create(String emailAddress) throws Exception {

        final Email email = new Email(emailAddress);

        assertThat(email.getEmailAddress()).isEqualTo(emailAddress);
    }

    @DisplayName("create - 이메일 형식에 맞지 않으면 에러")
    @ParameterizedTest
    @ValueSource(strings = {
            "email@email",
            "@email.com",
            "email.com"
    })
    void createInvalidEmail(String emailAddress) throws Exception {

        assertThatThrownBy(() -> new Email(emailAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 형식에 맞지 않습니다. emailAddress : [%s]", emailAddress);
    }

}