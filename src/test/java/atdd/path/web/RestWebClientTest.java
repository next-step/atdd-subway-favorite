package atdd.path.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class RestWebClientTest {
    private static final String NO_AUTHORIZATION = "";
    private WebTestClient webTestClient;

    public RestWebClientTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    <T> EntityExchangeResult<T> postMethodAcceptance(String uri, Object requestBody, Class<T> bodyClass) {
        return postMethodWithAuthAcceptance(uri, requestBody, bodyClass, NO_AUTHORIZATION);
    }

    <T> EntityExchangeResult<T> getMethodWithAuthAcceptance(String uri, Class<T> bodyClass, String jwt) {
        return this.webTestClient.get().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(bodyClass)
                .returnResult();
    }

    <T> EntityExchangeResult<T> postMethodWithAuthAcceptance(String uri, Object requestBody, Class<T> bodyClass, String jwt) {
        return webTestClient.post().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestBody), requestBody.getClass())
                .exchange()
                .expectBody(bodyClass)
                .returnResult();
    }

    <T> WebTestClient.BodyContentSpec getMethodWithAuthAcceptance(String uri, String jwt) {
        return this.webTestClient.get().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }


    <T> WebTestClient.BodyContentSpec postMethodWithAuthAcceptance(String uri, Object requestBody, String jwt) {
        return webTestClient.post().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestBody), requestBody.getClass())
                .exchange()
                .expectBody();
    }

    <T> EntityExchangeResult<Void> deleteMethodWithAuthAcceptance(String uri, String jwt) {
        return this.webTestClient.delete().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .exchange()
                .expectBody(Void.class)
                .returnResult();
    }
}
