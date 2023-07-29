package nextstep.subway.acceptance;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.study.AuthSteps.토큰_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_정보_조회됨;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

  public static final String EMAIL = "admin@email.com";
  public static final String PASSWORD = "password";
  public static final Integer AGE = 20;
  public static final String EMAIL1 = "sample@email.com";
  public static final String BAD_PASSWORD = "bad_password";
  private Long 강남역;
  private Long 양재역;
  private String 액세스토큰;
  private String 액세스토큰2;

  private Long 생성되지않은역 = 3L;

  @BeforeEach
  public void setUp() {
    super.setUp();
    회원_생성_요청(EMAIL, PASSWORD, AGE);
    ExtractableResponse response = 토큰_요청(EMAIL, PASSWORD);
    액세스토큰 = response.jsonPath().getString("accessToken");

    강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
    양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
  }
  @Nested
  @DisplayName("로그인에 성공 했을 때,")
  class SuccessfulLogin{

    /**
     * Given 로그인이 되어있을 때,
     * When 즐겨찾기를 생성하면
     * Then 즐겨찾기를 생성할 수 있다.
     */
    @DisplayName("로그인이 되어있을때, 즐겨찾기 생성")
    @Test
    void createFavoriteWithLogin() {
      // when
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, 액세스토큰);

      // then
      assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    /**
     * Given 로그인이 되어있을 때,
     * AND 즐겨찾기가 1개 추가되었고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기를 삭제할 수 있다.
     */
    @DisplayName("로그인이 되어있을때, 즐겨찾기 삭제할 수 있다.")
    @Test
    void deleteFavoriteWithLogin() {
      // given
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, 액세스토큰);
      Long id = creationResponse.jsonPath().getLong("id");
      // when
      ExtractableResponse deleteResponse = 즐겨찾기_삭제_요청(id, 액세스토큰);
      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    /**
     * Given 로그인이 되어있을 때,
     * AND 즐겨찾기가 1개 추가되었고
     * When 즐겨찾기를 조회하면
     * Then 1개의 즐겨찾기를 조회 할 수있다.
     */
    @DisplayName("로그인이 되어있을때, 즐겨찾기 조회할 수 있다.")
    @Test
    void getFavoriteWithLogin() {
      // given
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, 액세스토큰);
      Long id = creationResponse.jsonPath().getLong("id");
      // when
      ExtractableResponse getResponse = 즐겨찾기_조회_요청(액세스토큰);
      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      즐겨찾기_정보_조회됨(getResponse, 강남역, 양재역);
    }

    /**
     * Given 로그인이 되어있을 때,
     * AND 즐겨찾기가 1개 추가하고
     * When 다른 아이디로 로그인하고
     *         즐겨찾기를 삭제하면,
     * Then 처음의 즐겨찾기를 삭제 할 수 없다.
     */
    @DisplayName("로그인이 되어있을때, 다른 유저의 즐겨찾기를 삭제할 수 없다.")
    @Test
    void deleteFavoriteWithDifferentLogin() {
      // given
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, 액세스토큰);
      Long id = creationResponse.jsonPath().getLong("id");

      회원_생성_요청(EMAIL1, BAD_PASSWORD, 15);
      ExtractableResponse response = 토큰_요청(EMAIL1, BAD_PASSWORD);
      액세스토큰2 = response.jsonPath().getString("accessToken");

      // when
      ExtractableResponse deleteResponse = 즐겨찾기_삭제_요청(id, 액세스토큰2);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
  }

  @Nested
  @DisplayName("로그인에 실패 했을 때,")
  class FailedLogin{

    /**
     * Given 로그인이 유효하지 않을 때,
     * When 즐겨찾기를 생성하면
     * Then 즐겨찾기 생성에 실패한다.
     */
    @DisplayName("로그인이 되어있지않을때, 즐겨찾기 생성")
    @Test
    void createFavoriteWithoutLogin() {
// when
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, "");

      // then
      assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }
    /**
     * Given 로그인이 유효하지 않을 때,
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기 삭제가 실패한다.
     */
    @DisplayName("로그인이 되어있지않을때, 즐겨찾기 삭제")
    @Test
    void deleteFavoriteWithoutLogin() {
      // given
      // when
      ExtractableResponse deleteResponse = 즐겨찾기_삭제_요청(1L, "");
      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
    /**
     * Given 로그인이 유효하지 않을 때,
     * When 즐겨찾기를 조회하면
     * Then 즐겨찾기 조회가 실패한다.
     */
    @DisplayName("로그인이 되어있지않을때, 즐겨찾기 조회")
    @Test
    void getFavoriteWithoutLogin() {
      // given
      // when
      ExtractableResponse getResponse = 즐겨찾기_조회_요청("");
      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }
  }
  /**
   * Given 존재하지 않는 역의 id가 주어질 때,
   * When 추가되지 않은 역을 즐겨찾기로 생성하면
   * Then 즐겨찾기 생성이 실패한다.
   */
  @DisplayName("오류 케이스: 추가되지 않은 역을 즐겨찾기로 생성하면 즐겨찾기 생성이 실패한다.")
  @Test
  void createNotExistingFavorite() {
    // when
    ExtractableResponse creationResponse = 즐겨찾기_생성_요청(생성되지않은역, 양재역, 액세스토큰);

    // then
    assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }


}
