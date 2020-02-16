package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

    public static final String USER_URL = "/users";

    public UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @Test
    void 회원_가입() {
        // when
        UserResponseView response = userHttpTest.createUserTest().getResponseBody();

        // then
        assertThat(response.getName()).isEqualTo("브라운");
    }

    @Test
    void 회원_탈퇴() {
        // given
        UserResponseView response = userHttpTest.createUserTest().getResponseBody();

        // when
        webTestClient.delete().uri(USER_URL + "/" + response.getId())
                .exchange()
                .expectStatus().isNoContent();
    }
}
