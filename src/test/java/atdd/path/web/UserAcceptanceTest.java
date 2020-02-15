package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String BASE_URI="/user";
    public static final String NAME_IN_REQUEST="브라운";
    public static final String EMAIL_IN_REQUEST="boorwonie@email.com";
    public static final String PWD_IN_REQUEST="subway";
    public static final String NAME_IN_RESPONSE="$.name";
    public static final String EMAIL_IN_RESPONSE="$.email";
    public static final String PWD_IN_RESPONSE="$.password";

    @DisplayName("회원 등록이 제대로 되는가")
    @Test
    public void createUser(){
        //given
        CreateUserRequestView request = new CreateUserRequestView(EMAIL_IN_REQUEST, NAME_IN_REQUEST, PWD_IN_REQUEST);

        //when, then
        webTestClient.post().uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateUserRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath(NAME_IN_RESPONSE).isEqualTo(NAME_IN_REQUEST)
                .jsonPath(EMAIL_IN_RESPONSE).isEqualTo(EMAIL_IN_REQUEST)
                .jsonPath(PWD_IN_RESPONSE).isEqualTo(PWD_IN_REQUEST);
    }
}
