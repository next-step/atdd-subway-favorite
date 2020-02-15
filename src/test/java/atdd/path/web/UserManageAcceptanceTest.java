package atdd.path.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import reactor.core.publisher.Mono;

public class UserManageAcceptanceTest extends AbstractAcceptanceTest {
  private UserManageHttpTest userManageHttpTest;

  @BeforeEach
  void setUp() {
    this.userManageHttpTest = new UserManageHttpTest(webTestClient);
  }

  @Test
  public void createUser() {
    //When
    Long userID = userManageHttpTest.createNewUser(
        TEST_USER_1_EMAIL, 
        TEST_USER_1_NAME,
        TEST_USER_1_PASSWORD
        );

    EntityExchangeResult<UserResponseView> response = userManageHttpTest.retrieveUser(userID);

    //then
    assertThat(response.getResponseBody().getName()).isEqualTo(TEST_USER_1_NAME);
    assertThat(response.getResponseBody().getEmail()).isEqualTo(TEST_USER_1_EMAIL);
    assertThat(response.getResponseBody().getPassword()).isEqualTo(TEST_USER_1_PASSWORD);
  }

  @Test
  public void retrieveUser() {
    //Given
    Long userID = userManageHttpTest.createNewUser(
        TEST_USER_1_EMAIL, 
        TEST_USER_1_NAME,
        TEST_USER_1_PASSWORD
        );

    //When
    EntityExchangeResult<UserResponseView> response = userManageHttpTest.retrieveUser(userID);

    //then
    assertThat(response.getResponseBody().getName()).isEqualTo(TEST_USER_1_NAME);
    assertThat(response.getResponseBody().getEmail()).isEqualTo(TEST_USER_1_EMAIL);
    assertThat(response.getResponseBody().getPassword()).isEqualTo(TEST_USER_1_PASSWORD);
  }
}
