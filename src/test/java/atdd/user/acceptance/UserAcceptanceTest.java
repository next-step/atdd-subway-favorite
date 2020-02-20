package atdd.user.acceptance;

import atdd.AbstractAcceptanceTest;
import atdd.user.controller.UserController;
import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    private final String email = "email@email.com";
    private final String name = "name!!";
    private final String password = "password!!";

    private UserHttpTestSupport userHttpTestSupport;

    @BeforeEach
    void setup() {
        this.userHttpTestSupport = new UserHttpTestSupport(webTestClient);
    }

    @DisplayName("회원 가입")
    @Test
    void create() throws Exception {

        final UserCreateRequestDto requestDto = UserCreateRequestDto.of(email, name, password);
        final EntityExchangeResult<UserResponseDto> result = webTestClient.post()
                .uri(UserController.ROOT_URI)
                .body(Mono.just(requestDto), UserCreateRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult();

        final UserResponseDto responseDto = result.getResponseBody();
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getEmail()).isEqualTo(email);
        assertThat(responseDto.getPassword()).isEqualTo(password);

        final String location = result.getResponseHeaders().getLocation().toString();
        assertThat(location).isEqualTo(UserController.ROOT_URI + "/" + responseDto.getId());
    }


    @DisplayName("회원 탈퇴")
    @Test
    void delete() throws Exception {
        final UserCreateRequestDto requestDto = UserCreateRequestDto.of(email, name, password);
        final UserResponseDto createdUser = userHttpTestSupport.create(requestDto);

        webTestClient.delete()
                .uri(UserController.ROOT_URI + "/" + createdUser.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(UserResponseDto.class)
                .returnResult();
    }

}
