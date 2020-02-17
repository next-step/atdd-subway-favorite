package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @Test
    public void userCreate() {
        CreateUserRequestView view = CreateUserRequestView.builder()
                .name(USER_NAME1)
                .email(USER_EMAIL1)
                .password(USER_PASSWORD1).build();

        User user = userHttpTest.createUserRequest(view)
                .getResponseBody();

        assertThat(user.getName()).isEqualTo(USER_NAME1);
        assertThat(user.getEmail()).isEqualTo(USER_EMAIL1);
        assertThat(user.getPassword()).isEqualTo(USER_PASSWORD1);
    }
}
