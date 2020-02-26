package atdd.path;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class AbstractHttpTest {

    public WebTestClient webTestClient;

    public AbstractHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    protected <T> EntityExchangeResult<T> createRequest(Class<T> classT, String uri, String inputJson) {
        return webTestClient.post().uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(classT)
                .returnResult();
    }

    protected <T> EntityExchangeResult<T> createRequestWithToken(Class<T> classT, String uri, String token) {
        return webTestClient.post().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(classT)
                .returnResult();
    }

    protected <T> EntityExchangeResult<T> findRequest(Class<T> classT, String uri) {
        return webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(classT)
                .returnResult();
    }

    protected <T> EntityExchangeResult<List<T>> findRequestList(Class<T> classT, String uri) {
        return webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(classT)
                .returnResult();
    }

    protected <T> EntityExchangeResult<T> findRequestWithToken(Class<T> classT, String uri, String token) {
        return webTestClient.get().uri(uri)
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(classT)
                .returnResult();
    }

}
