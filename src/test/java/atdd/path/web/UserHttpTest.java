package atdd.path.web;

import atdd.path.application.dto.UserResponseView;
import atdd.path.auth.JwtTokenDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    private EntityExchangeResult<UserResponseView> createUserTest(String reqUri, String inputJson) {
        return webTestClient
                .post()
                .uri(reqUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(UserResponseView.class).consumeWith(result -> createUserAssert(result, reqUri))
                .returnResult();
    }

    public Long createUserSuccess(String reqUri, String inputJson) {
        EntityExchangeResult<UserResponseView> responseEntity = createUserTest(reqUri, inputJson);
        return responseEntity.getResponseBody().getId();
    }

    public WebTestClient.ResponseSpec showUsersAndSingleUserTest(String reqUri) {
        return webTestClient
                .get()
                .uri(reqUri)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    public StatusAssertions createAuthorizationTokenTest(String reqUri, String inputJson) {
        return webTestClient
                .post()
                .uri(reqUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus();
    }

    public String createAuthorizationTokenSuccess(String reqUri, String inputJson) {
        EntityExchangeResult<JwtTokenDTO> getTokenResponse = createAuthorizationTokenTest(reqUri, inputJson).isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(JwtTokenDTO.class).consumeWith(this::createTokenResultAssert)
                .returnResult();

        String accessToken = getTokenResponse.getResponseBody().getAccessToken();
        String tokenType = getTokenResponse.getResponseBody().getTokenType();

        String tokenInfo = tokenType + " " + accessToken;

        return tokenInfo;
    }

    private void createTokenResultAssert(EntityExchangeResult<JwtTokenDTO> result) {
        assertThat(result.getResponseBody().getAccessToken()).isNotEmpty();
        assertThat(result.getResponseBody().getTokenType()).isNotEmpty();
    }

    private void createUserAssert(EntityExchangeResult<UserResponseView> result, String reqUri) {
        HttpHeaders responseHeaders = result.getResponseHeaders();
        URI location = responseHeaders.getLocation();
        String stringifyLocation = location.toString();
        assertThat(stringifyLocation).isEqualTo(reqUri + "/" + result.getResponseBody().getId());
    }
}
