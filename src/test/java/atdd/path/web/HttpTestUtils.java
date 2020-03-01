package atdd.path.web;

import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.domain.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static atdd.path.TestConstant.ACCESS_TOKEN_HEADER;

public class HttpTestUtils {
    private WebTestClient webTestClient;
    private UserHttpTest userHttpTest;

    public HttpTestUtils(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    public <T> EntityExchangeResult postRequest(String uri, String inputJson, String accessToken, Class<T> bodyType) {
        return webTestClient.post().uri(uri)
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(bodyType).returnResult();
    }

    public EntityExchangeResult postRequest(String uri, String inputJson, String accessToken) {
        return webTestClient.post().uri(uri)
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();
    }

    public <T> EntityExchangeResult getRequest(String uri, String accessToken, ParameterizedTypeReference<T> bodyType) {
        return webTestClient.get().uri(uri)
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(bodyType).returnResult();
    }

    public void getRequestNotFound(String uri, String accessToken) {
        webTestClient.get().uri(uri)
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .exchange()
                .expectStatus().isNotFound();
    }

    public void deleteRequest(String uri, String accessToken) {
        webTestClient.delete().uri(uri)
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().returnResult();
    }

    public User createGivenUser(CreateUserRequestView view) {
        return userHttpTest.createUserRequest(view).getResponseBody();
    }

    public String createGivenAccessToken(User user) {
        return userHttpTest.givenLogin(user);
    }
}
