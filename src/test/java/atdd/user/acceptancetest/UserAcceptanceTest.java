package atdd.user.acceptancetest;

import atdd.AbstractAcceptanceTest;
import atdd.user.controller.UserController;
import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("회원 가입")
    @Test
    void create() throws Exception {
        final String email = "email@email.com";
        final String name = "name!!";
        final String password = "password!!";


        final UserCreateRequestDto requestDto = UserCreateRequestDto.of(email, name, password);
        final EntityExchangeResult<UserResponseDto> result = webTestClient.post()
                .uri(UserController.ROOT_URI)
                .body(Mono.just(requestDto), UserCreateRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult();

        final String location = result.getResponseHeaders().getLocation().toString();
        assertThat(location).isEqualTo(UserController.ROOT_URI + "/1");

        final UserResponseDto responseDto = result.getResponseBody();
        assertThat(responseDto.getId()).isNull();
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getEmail()).isEqualTo(email);
        assertThat(responseDto.getPassword()).isEqualTo(password);
    }

}
