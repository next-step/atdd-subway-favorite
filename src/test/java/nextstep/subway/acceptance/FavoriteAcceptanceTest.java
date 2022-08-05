package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class FavoriteAcceptanceTest extends AcceptanceTest {


  private Long 강남역;
  private Long 양재역;

  @BeforeEach
  public void setUp() {
    super.setUp();

    강남역 = 지하철역_생성_요청(관리자토큰, "강남역").jsonPath().getLong("id");
    양재역 = 지하철역_생성_요청(관리자토큰, "양재역").jsonPath().getLong("id");
  }

  /**
   * When 로그인한 회원이 즐겨찾기를 등록하면
   * Then 즐겨찾기가 추가된다
   */
  @Test
  void 즐겨찾기_생성() {
    // when
    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(관리자토큰, 강남역, 양재역);

    assertAll(
        () -> assertEquals(response.statusCode(), HttpStatus.CREATED.value())
    );

    // then
    ExtractableResponse<Response> result = 즐겨찾기_조회_요청(관리자토큰);

    List<String> source = result.jsonPath().getList("source.name");
    List<String> target = result.jsonPath().getList("target.name");

    assertAll(
        () -> assertEquals(result.statusCode(), HttpStatus.OK.value()),
        () -> assertThat(source).containsExactly("강남역"),
        () -> assertThat(target).containsExactly("양재역")
    );
  }

  /**
   * When 미로그인한 회원이 즐겨찾기를 등록하면
   * Then 에러 발생
   */
  @Test
  void 즐겨찾기_생성_미로그인_권한_에러() {
    // when
    ExtractableResponse<Response> response = 즐겨찾기_생성_요청("", 강남역, 양재역);

    // then
    assertAll(
        () -> assertEquals(response.statusCode(), HttpStatus.UNAUTHORIZED.value())
    );
  }

  /**
   * When 로그인한 회원이 같은 역을 즐겨찾기를 등록하면
   * Then 에러 발생
   */
  @Test
  void 즐겨찾기_생성_같은역_등록_에러() {
    // when
    ExtractableResponse<Response> result = 즐겨찾기_생성_요청(관리자토큰, 강남역, 강남역);

    // then
    assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  /**
   * When 미로그인한 회원이 즐겨찾기를 등록하면
   * Then 에러 발생
   */
  @Test
  void 즐겨찾기_조회_미로그인_권한_에러() {
    // when
    ExtractableResponse<Response> result = 즐겨찾기_조회_요청("");

    // then
    assertAll(
        () -> assertEquals(result.statusCode(), HttpStatus.UNAUTHORIZED.value())
    );
  }

  /**
   * Given 로그인한 회원이 즐겨찾기 등록을 요청 하고
   * When 즐겨찾기 제거를 요청 하면
   * Then 즐겨찾기가 제거된다
   */
  @Test
  void 즐겨찾기_삭제() {
    // given
    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(관리자토큰, 강남역, 양재역);

    // when
    ExtractableResponse<Response> result = 즐겨찾기_삭제_요청(관리자토큰, response);

    // then
    assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  /**
   * Given 로그인한 회원이 즐겨찾기 등록을 요청 하고
   * When 미로그인한 회원이 즐겨찾기를 제거하면
   * Then 에러 발생
   */
  @Test
  void 즐겨찾기_삭제_미로그인_권한_에러() {
    // given
    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(관리자토큰, 강남역, 양재역);

    // when
    ExtractableResponse<Response> result = 즐겨찾기_삭제_요청("", response);

    // then
    assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }
}
