package atdd.path.web;

import atdd.path.application.dto.UserResponseDto;
import atdd.path.web.dto.UserCreateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.UserAcceptanceTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends UserAcceptanceTestSupport {

    @DisplayName("회원가입을 한다.")
    @Test
    public void createUser() {
        // given
        UserCreateRequestDto createRequestDto = UserCreateRequestDto.of("serverwizard@woowahan.com", "홍종완", "1234");

        // when
        Long createdUserId = createUserResource(createRequestDto);

        // then
        UserResponseDto result = getUserResource(createdUserId);
        assertThat(result.getEmail()).isEqualTo("serverwizard@woowahan.com");
        assertThat(result.getName()).isEqualTo("홍종완");
    }

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    public void deleteUser() {
        // given
        UserCreateRequestDto createRequestDto = UserCreateRequestDto.of("serverwizard@woowahan.com", "홍종완", "1234");
        Long createdUserId = createUserResource(createRequestDto);

        // when
        deleteUserResource(createdUserId);

        webTestClient.get()
                .uri("/users/" + createdUserId)
                .exchange()
                .expectStatus().isBadRequest();
    }

}
