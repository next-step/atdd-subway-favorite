package nextstep.subway.favorite;

import static nextstep.subway.favorite.FavoriteSteps.로그인되어있지_않음;
import static nextstep.subway.favorite.FavoriteSteps.즐겨찾기_경로추가_요청;
import static nextstep.subway.favorite.FavoriteSteps.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.FavoriteSteps.즐겨찾기_목록_조회요청;
import static nextstep.subway.favorite.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.FavoriteSteps.즐겨찾기_삭제됨;
import static nextstep.subway.favorite.FavoriteSteps.즐겨찾기에_경로추가됨;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 인수테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

  StationResponse 광교역;
  StationResponse 강남역;
  StationResponse 광교중앙역;
  LineResponse 신분당선;
  TokenResponse 토큰 = new TokenResponse("UNAUTHORIZED");

  public static final String EMAIL = "email@email.com";
  public static final String PASSWORD = "password";
  public static final String NEW_EMAIL = "newemail@email.com";
  public static final String NEW_PASSWORD = "newpassword";
  public static final int AGE = 20;
  public static final int NEW_AGE = 21;

  @BeforeEach
  void init() {
    강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
    광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
    광교중앙역 = 지하철역_등록되어_있음("광교중앙역").as(StationResponse.class);
    Map<String, String> lineCreateParams = createLineCreateParams();
    신분당선 = 지하철_노선_등록되어_있음(lineCreateParams).as(LineResponse.class);
    지하철_노선에_지하철역_등록_요청(신분당선, 광교역, 광교중앙역, 5);
    회원_생성_요청(EMAIL, PASSWORD, AGE);
  }

  private Map<String, String> createLineCreateParams() {
    Map<String, String> lineCreateParams = new HashMap<>();
    lineCreateParams.put("name", "신분당선");
    lineCreateParams.put("color", "bg-red-600");
    lineCreateParams.put("upStationId", 강남역.getId() + "");
    lineCreateParams.put("downStationId", 광교역.getId() + "");
    lineCreateParams.put("distance", 10 + "");
    return lineCreateParams;
  }




  @DisplayName("Feature : 즐겨찾기를 관리한다")
  @Nested
  class FeatureFavoriteManage {

    @DisplayName("Scnario : 로그인 되어 있는 상태에서 즐겨찾기 관리 기능 테스트")
    @Test
    void manageFavorite() {
      토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
      ExtractableResponse<Response> 즐겨찾기_추가_응답 =  즐겨찾기_경로추가_요청(토큰,광교역.getId(),강남역.getId());
      String url = 즐겨찾기_추가_응답.header("Location");
      즐겨찾기에_경로추가됨(즐겨찾기_추가_응답);
      ExtractableResponse<Response> 즐겨찾기_목록_조회응답 =즐겨찾기_목록_조회요청(토큰);
      즐겨찾기_목록_조회됨(즐겨찾기_목록_조회응답);
      ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(토큰,url);
      즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }

    @DisplayName("Scnario : 로그인되지 않은 상태에서 즐겨찾기 관리 기능 테스트")
    @Test
    void manageFavoriteWithUnauthenticated(){
      ExtractableResponse<Response> 즐겨찾기_추가_응답 =  즐겨찾기_경로추가_요청(토큰,광교역.getId(),강남역.getId());
      로그인되어있지_않음(즐겨찾기_추가_응답);
      ExtractableResponse<Response> 즐겨찾기_목록_조회응답 =즐겨찾기_목록_조회요청(토큰);
      로그인되어있지_않음(즐겨찾기_목록_조회응답);
      ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(토큰,"/favorites/1");
      로그인되어있지_않음(즐겨찾기_삭제_응답);
    }
  }

}
