package nextstep.favorite.acceptance;

import static nextstep.Fixtures.*;
import static nextstep.favorite.acceptance.steps.FavoriteAcceptanceSteps.*;
import static nextstep.member.acceptance.steps.AuthAcceptanceSteps.로그인_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.support.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 관련 기능 인수 테스트")
class FavoriteAcceptanceTest extends AcceptanceTest {
  @Autowired private StationRepository stationRepository;
  @Autowired private LineRepository lineRepository;
  @Autowired private MemberRepository memberRepository;

  private Station 교대역;
  private Station 남부터미널역;
  private Station 양재역;
  private String accessToken;

  /** Given 지하철 역과 노선이 등록되어 있고 사용자 또한 로그인 되어 있다. */
  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();
    교대역 = stationRepository.save(교대역());
    남부터미널역 = stationRepository.save(남부터미널역());
    양재역 = stationRepository.save(양재역());
    lineRepository.save(
        aLine()
            .name("3호선")
            .color("bg-orange-600")
            .lineSections(
                new LineSections(LineSection.of(교대역, 남부터미널역, 2), LineSection.of(남부터미널역, 양재역, 3)))
            .build());
    Member member = memberRepository.save(aMember().build());
    accessToken = 로그인_요청(member.getEmail(), member.getPassword());
  }

  /** When 즐겨찾기를 추가하면 Then 즐겨찾기 목록에 추가한 즐겨찾기가 조회된다. */
  @DisplayName("즐겨찾기를 추가한다.")
  @Test
  void addFavorite() {
    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(교대역, 양재역, accessToken);

    즐겨찾기_생성됨(response);
    즐겨찾기_목록에_포함됨(즐겨찾기_목록_조회_요청(accessToken), Collections.singletonList(response));
  }

  /** Given 즐겨찾기가 등록되어 있고 When 즐겨찾기 목록을 조회하면 Then 즐겨찾기 목록이 조회된다. */
  @DisplayName("즐겨찾기 목록을 조회한다.")
  @Test
  void listFavorites() {
    ExtractableResponse<Response> 즐겨찾기1 = 즐겨찾기_생성_요청(교대역, 양재역, accessToken);
    ExtractableResponse<Response> 즐겨찾기2 = 즐겨찾기_생성_요청(양재역, 남부터미널역, accessToken);

    ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);

    즐겨찾기_목록에_포함됨(response, Arrays.asList(즐겨찾기1, 즐겨찾기2));
  }

  /** When 즐겨찾기를 삭제하면 Then 즐겨찾기 목록에서 삭제한 즐겨찾기가 조회되지 않는다. */
  @DisplayName("즐겨찾기를 삭제한다.")
  @Test
  void deleteFavorite() {
    ExtractableResponse<Response> response = 즐겨찾기_생성_요청(교대역, 양재역, accessToken);
    String uri = response.header(HttpHeaders.LOCATION);

    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(uri, accessToken);

    즐겨찾기_삭제됨(uri, response, accessToken);
  }
}
