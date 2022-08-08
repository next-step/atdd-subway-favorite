package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.common.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

  static final String MEMBER_EMAIL = "test@email.com";
  static final String PASSWORD = "password";

  @Autowired
  private StationService stationService;

  @Autowired
  private FavoriteService favoriteService;

  private StationResponse 강남역;
  private StationResponse 역삼역;

  private FavoriteRequest favoriteRequest;

  @BeforeEach
  public void setUp() {
    강남역 = stationService.saveStation(new StationRequest("강남역"));
    역삼역 = stationService.saveStation(new StationRequest("역삼역"));

    favoriteRequest = new FavoriteRequest(강남역.getId(), 역삼역.getId());
  }

  @Test
  void 즐겨찾기_생성() {
    FavoriteResponse result = favoriteService.saveFavorite(favoriteRequest, MEMBER_EMAIL);

    assertThat(result.getSource().getName()).isEqualTo("강남역");
    assertThat(result.getTarget().getName()).isEqualTo("역삼역");
  }

  @Test
  void 즐겨찾기_생성_같은역_등록_에러() {
    favoriteRequest = new FavoriteRequest(강남역.getId(), 강남역.getId());
    assertThatThrownBy(() -> favoriteService.saveFavorite(favoriteRequest, MEMBER_EMAIL)).isInstanceOf(CustomException.class);
  }

  @Test
  void 즐겨찾기_조회() {
    favoriteService.saveFavorite(favoriteRequest, MEMBER_EMAIL);

    List<FavoriteResponse> result = favoriteService.getFavorite(MEMBER_EMAIL);

    assertThat(result.get(0).getSource().getName()).isEqualTo("강남역");
    assertThat(result.get(0).getTarget().getName()).isEqualTo("역삼역");
  }
}
