package nextstep.subway.acceptance;

import static nextstep.study.AuthSteps.토큰_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_정보_조회됨;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

  public static final String EMAIL = "admin@email.com";
  public static final String PASSWORD = "password";
  public static final Integer AGE = 20;
  private Long 강남역;
  private Long 양재역;
  private String 액세스토큰;

  private Long 생성되지않은역 = 3L;
  @Autowired
  private MemberRepository memberRepository;
  @Nested
  @DisplayName("로그인에 성공 했을 때,")
  class SuccessfulLogin{
    String accessToken;
    @BeforeEach
    void setUp() {
      memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
      ExtractableResponse response = 토큰_요청(EMAIL, PASSWORD);
      액세스토큰 = response.jsonPath().getString("accessToken");

      강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
      양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
    }
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
    @DisplayName("로그인이 되어있을때, 즐겨찾기 삭제할 수 있다.")
    @Test
    void getFavoriteWithLogin() {
      // given
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, 액세스토큰);
      Long id = creationResponse.jsonPath().getLong("id");
      // when
      ExtractableResponse getResponse = 즐겨찾기_조회_요청(액세스토큰);
      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      즐겨찾기_정보_조회됨(getResponse,id,강남역,양재역);
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
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, 액세스토큰);

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
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, 액세스토큰);
      Long id = creationResponse.jsonPath().getLong("id");
      // when
      ExtractableResponse deleteResponse = 즐겨찾기_삭제_요청(id, 액세스토큰);
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
      ExtractableResponse creationResponse = 즐겨찾기_생성_요청(강남역, 양재역, 액세스토큰);
      Long id = creationResponse.jsonPath().getLong("id");
      // when
      ExtractableResponse getResponse = 즐겨찾기_조회_요청(액세스토큰);
      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
      즐겨찾기_정보_조회됨(getResponse,id,강남역,양재역);

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
    assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
  }

}
