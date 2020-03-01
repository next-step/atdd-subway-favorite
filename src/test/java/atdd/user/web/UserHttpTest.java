package atdd.user.web;

import atdd.Constant;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static atdd.Constant.USER_BASE_URI;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public Long createUser(String EMAIL_IN_REQUEST, String NAME_IN_REQUEST, String PWD_IN_REQUEST) {
        CreateUserRequestView request = new CreateUserRequestView(EMAIL_IN_REQUEST, NAME_IN_REQUEST, PWD_IN_REQUEST);
        return webTestClient.post().uri(USER_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateUserRequestView.class)
                .exchange()
                .returnResult(UserResponseView.class)
                .getResponseBody()
                .toStream()
                .map(UserResponseView::getId)
                .collect(Collectors.toList())
                .get(0);
    }
}
