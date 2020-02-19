package atdd.path.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    public void createUser() {
        String userEmail = "boorwonie@email.com";
        String userName = "브라운";
        String password = "subway";

        User user = new User(userEmail, userName, password);

        assertThat(user.getName()).isEqualTo("브라운");
    }
}
