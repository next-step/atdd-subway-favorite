package nextstep.favorite.application;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;
import java.util.Optional;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.auth.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.ui.BusinessException;
import nextstep.utils.FixtureUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

  @Autowired
  StationRepository stationRepository;

  @Autowired
  SectionRepository sectionRepository;

  @Autowired
  LineRepository lineRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  FavoriteRepository favoriteRepository;

  @Autowired
  FavoriteService favoriteService;

  Line 신분당선;
  Line 부산1호선;
  Station 강남역;
  Station 양재역;
  Station 서면역;
  Station 남포역;
  List<Section> 구간목록;
  LoginMember 인증정보;
  Member 멤버;

  /**
   * Given 역과 노선(+구간) 그리고 회원 정보를 생성한다.
   */
  @BeforeEach
  public void setUp() {
    신분당선 = 노선_생성("신분당선", "빨강");
    부산1호선 = 노선_생성("부산1호선", "주황");
    강남역 = 역_생성("강남역");
    양재역 = 역_생성("양재역");
    서면역 = 역_생성("서면역");
    남포역 = 역_생성("남포역");
    구간목록 = List.of(
      구간_생성(신분당선, 강남역, 양재역, 10),
      구간_생성(부산1호선, 서면역, 남포역, 5)
    );

    멤버 = 멤버_생성();
    인증정보 = new LoginMember(멤버.getEmail());
  }

  @DisplayName("즐겨찾기 생성 성공")
  @Test
  void 즐겨찾기_생성_성공() {
    // given
    var request = new FavoriteRequest(강남역.getId(), 양재역.getId());

    // when
    final var result = favoriteService.createFavorite(인증정보.getEmail(), request);

    // then
    Optional<Favorite> favorite = favoriteRepository.findById(result);
    assertThat(favorite.isPresent()).isTrue();
    assertThat(favorite.get().getMember()).isEqualTo(멤버);
    assertThat(favorite.get().getSource()).isEqualTo(강남역);
    assertThat(favorite.get().getTarget()).isEqualTo(양재역);
  }

  @DisplayName("즐겨찾기 생성 실패 - 출발역과 도착역이 같은 경로를 즐겨찾기 할 수 없음")
  @Test
  void 즐겨찾기_생성_실패_출발역과_도착역이_같은_경로를_즐겨찾기_할_수_없음() {
    // given
    var request = new FavoriteRequest(강남역.getId(), 강남역.getId());

    // when
    final var result = catchThrowable(() -> favoriteService.createFavorite(인증정보.getEmail(), request));

    // then
    assertThat(result).isInstanceOf(BusinessException.class)
        .hasMessageContaining("출발역과 도착역을 다르게 설정해주세요.");
  }

  @DisplayName("즐겨찾기 생성 실패 - 출발역 혹은 도착역을 찾을 수 없음")
  @Test
  void 즐겨찾기_생성_실패_출발역_혹은_도착역을_찾을_수_없음() {
    // given
    var 등록하지_않은_역_ID = -1L;
    var request = new FavoriteRequest(등록하지_않은_역_ID, 등록하지_않은_역_ID);

    // when
    final var result = catchThrowable(() -> favoriteService.createFavorite(인증정보.getEmail(), request));

    // then
    assertThat(result).isInstanceOf(BusinessException.class)
        .hasMessageContaining("역 정보를 찾을 수 없습니다.");
  }

  @DisplayName("즐겨찾기 생성 실패 - 이어지지 않는 경로")
  @Test
  void 즐겨찾기_생성_실패_이어지지_않는_경로() {
    // given
    var request = new FavoriteRequest(강남역.getId(), 서면역.getId());

    // when
    final var result = catchThrowable(() -> favoriteService.createFavorite(인증정보.getEmail(), request));

    // then
    assertThat(result).isInstanceOf(BusinessException.class)
        .hasMessageContaining("이어지지 않는 경로입니다.");
  }

  @DisplayName("즐겨찾기 조회 성공")
  @Test
  void 즐겨찾기_조회_성공() {
    // given
    var 즐겨찾기 = 즐겨찾기_생성(멤버, 강남역, 양재역);

    // when
    var result = favoriteService.findFavoriteByMemberEmail(인증정보.getEmail(), 즐겨찾기.getId());

    // then
    assertThat(result.getId()).isEqualTo(즐겨찾기.getId());
    assertThat(result.getSource().getId()).isEqualTo(즐겨찾기.getSource().getId());
    assertThat(result.getTarget().getId()).isEqualTo(즐겨찾기.getTarget().getId());
  }

  @DisplayName("즐겨찾기 조회 실패 - 등록되지 않은 즐겨찾기")
  @Test
  void 즐겨찾기_조회_실패_등록되지_않은_즐겨찾기() {
    // given
    var 등록되지_않은_즐겨찾기_ID = -1L;

    // when
    var result = catchThrowable(() -> favoriteService.findFavoriteByMemberEmail(인증정보.getEmail(), 등록되지_않은_즐겨찾기_ID));

    // then
    assertThat(result).isInstanceOf(BusinessException.class)
        .hasMessageContaining("즐겨찾기 정보를 찾을 수 없습니다.");
  }

  @DisplayName("즐겨찾기 조회 실패 - 다른 사람이 등록한 즐겨찾기")
  @Test
  void 즐겨찾기_조회_실패_다른_사람이_등록한_즐겨찾기() {
    // given
    var 다른멤버 = 멤버_생성();
    var 즐겨찾기 = 즐겨찾기_생성(다른멤버, 강남역, 양재역);

    // when
    var result = catchThrowable(() -> favoriteService.findFavoriteByMemberEmail(인증정보.getEmail(), 즐겨찾기.getId()));

    // then
    assertThat(result).isInstanceOf(BusinessException.class)
        .hasMessageContaining("즐겨찾기 정보를 찾을 수 없습니다.");
  }

  @DisplayName("즐겨찾기 목록 조회 성공")
  @Test
  void 즐겨찾기_목록_조회_성공() {
    // given
    var 신분당선_즐겨찾기 = 즐겨찾기_생성(멤버, 강남역, 양재역);
    var 부산1호선_즐겨찾기 = 즐겨찾기_생성(멤버, 남포역, 서면역);

    // when
    var result = favoriteService.findFavoritesByMemberEmail(인증정보.getEmail());

    // then
    assertThat(result).hasSize(2);
    assertThat(result.stream().map(FavoriteResponse::getId)).contains(신분당선_즐겨찾기.getId(), 부산1호선_즐겨찾기.getId());
  }

  @DisplayName("즐겨찾기 목록 조회 실패 - 멤버를 찾을 수 없음")
  @Test
  void 즐겨찾기_목록_조회_실패_멤버를_찾을_수_없음() {
    // given
    var 등록하지_않은_멤버 = "Unregistered Email";

    // when
    var result = catchThrowable(() -> favoriteService.findFavoritesByMemberEmail(등록하지_않은_멤버));

    // then
    assertThat(result).isInstanceOf(AuthenticationException.class)
            .hasMessageContaining("유효한 인증 토큰이 아닙니다.");
  }

  private Station 역_생성(String name) {
    return stationRepository.save(new Station(name));
  }

  private Section 구간_생성(Line line, Station upStation, Station downStation, int distance) {
    return sectionRepository.save(new Section(line, upStation, downStation, distance));
  }

  private Line 노선_생성(String name, String color) {
    final var 노선 = FixtureUtil.getBuilder(Line.class)
        .set("name", name)
        .set("color", color)
        .set("sections", null)
        .sample();

    return lineRepository.save(노선);
  }

  private Member 멤버_생성() {
    final var 멤버 = FixtureUtil.getFixture(Member.class);

    return memberRepository.save(멤버);
  }

  private Favorite 즐겨찾기_생성(Member member, Station source, Station target) {
    var 즐겨찾기 = FixtureUtil.getBuilder(Favorite.class)
        .set(javaGetter(Favorite::getMember), member)
        .set(javaGetter(Favorite::getSource), source)
        .set(javaGetter(Favorite::getTarget), target)
        .sample();

    return favoriteRepository.save(즐겨찾기);
  }
}