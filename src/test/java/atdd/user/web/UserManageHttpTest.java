package atdd.user.web;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import atdd.user.application.dto.UserResponseView;
import reactor.core.publisher.Mono;

public class UserManageHttpTest {
  public WebTestClient webTestClient;

  public UserManageHttpTest(WebTestClient webTestClient) {
    this.webTestClient = webTestClient;
  }

  public Long createNewUser(String email, String name, String password) {
    String requestBody = "{" 
      + "\"email\" : \"" + email + "\","
      + "\"name\" : \"" + name + "\","
      + "\"password\" : \"" + password + "\""
      + "}";

    EntityExchangeResult<UserResponseView> createdUser = webTestClient
      .post().uri("/user")
      .contentType(MediaType.APPLICATION_JSON)
      .body(Mono.just(requestBody), String.class)
      .exchange()
      .expectStatus().isCreated()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectHeader().exists("Location")
      .expectBody(UserResponseView.class)
      .returnResult();

    return createdUser.getResponseBody().getId();
  }

  public EntityExchangeResult<UserResponseView> retrieveUser(Long id) {
    return webTestClient
      .get().uri("/user/" + id.toString())
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody(UserResponseView.class)
      .returnResult();
  }
}
