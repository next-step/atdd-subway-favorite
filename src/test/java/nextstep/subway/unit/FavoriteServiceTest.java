package nextstep.subway.unit;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

  private static final Long ACTIVE_USER_ID = 1L;

  private Station 교대역;
  private Station 강남역;
  private Station 양재역;
  private Station 남부터미널역;
  private Line 신분당선;
  private Line 이호선;
  private Line 삼호선;

  @Autowired
  private StationRepository stationRepository;

  @Autowired
  private LineRepository lineRepository;

  @Autowired
  private FavoriteService favoriteService;

  private static void 즐겨찾기_조회_성공(List<FavoriteResponse> response, Long source, Long target) {
    assertThat(response.size()).isEqualTo(1);
    assertThat(response.stream().findFirst().map(FavoriteResponse::getSource).map(StationResponse::getId).get()).isEqualTo(source);
    assertThat(response.stream().findFirst().map(FavoriteResponse::getTarget).map(StationResponse::getId).get()).isEqualTo(target);
  }

  @BeforeEach
  void setUp() {
    교대역 = stationRepository.save(new Station("교대역"));
    강남역 = stationRepository.save(new Station("강남역"));
    양재역 = stationRepository.save(new Station("양재역"));
    남부터미널역 = stationRepository.save(new Station("남부터미널역"));

    신분당선 = lineRepository.save(new Line("신분당선", "red"));
    이호선 = lineRepository.save(new Line("2호선", "red"));
    삼호선 = lineRepository.save(new Line("3호선", "red"));

    신분당선.addSection(강남역, 양재역, 3);
    이호선.addSection(교대역, 강남역, 3);
    삼호선.addSection(교대역, 남부터미널역, 5);
    삼호선.addSection(남부터미널역, 양재역, 5);
  }

  @Test
  @DisplayName("즐겨찾기 추가 성공")
  void addFavoriteTest() {
    // When
    Long savedId = favoriteService.saveFavorite(ACTIVE_USER_ID, new FavoriteRequest(교대역.getId(), 강남역.getId()));

    // Then
    assertThat(savedId).isNotNull();
  }

  @Test
  @DisplayName("삭제된 역으로 즐겨찾기 추가 시 실패")
  void invalidSourceAndTargetTest() {
    // Given
    final Long 인덕원역 = stationRepository.save(new Station("인덕원역")).getId();
    final Long 정부과천청사역 = stationRepository.save(new Station("정부과천청사역")).getId();
    stationRepository.deleteById(인덕원역);
    stationRepository.deleteById(정부과천청사역);

    // When & Then
    assertThatThrownBy(
      () -> favoriteService.saveFavorite(ACTIVE_USER_ID, new FavoriteRequest(인덕원역, 정부과천청사역))
    ).isInstanceOf(IllegalArgumentException.class);

  }

  @Test
  @DisplayName("출발 역, 도착 역이 동일한 경우 즐겨찾기 추가 시 실패")
  void addSameSourceAndTargetTest() {
    // When & Then
    assertThatThrownBy(
      () -> favoriteService.saveFavorite(ACTIVE_USER_ID, new FavoriteRequest(교대역.getId(), 교대역.getId()))
    ).isInstanceOf(IllegalArgumentException.class);

  }

  @Test
  @DisplayName("즐겨찾기 조회 성공")
  void successfulSearchTest() {
    // Given
    favoriteService.saveFavorite(ACTIVE_USER_ID, new FavoriteRequest(교대역.getId(), 강남역.getId()));

    // When
    List<FavoriteResponse> response = favoriteService.searchFavorites(ACTIVE_USER_ID);

    // Then
    즐겨찾기_조회_성공(response, 교대역.getId(), 강남역.getId());
  }

  @Test
  @DisplayName("즐겨찾기 삭제 성공")
  void successfulDeleteTest() {
    // Given
    Long id = favoriteService.saveFavorite(ACTIVE_USER_ID, new FavoriteRequest(교대역.getId(), 강남역.getId()));

    favoriteService.deleteFavorite(id);
  }

}
