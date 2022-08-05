package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.member.domain.RoleType;
import nextstep.member.domain.User;
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

  private User user;

  private StationResponse 강남역;
  private StationResponse 역삼역;

  @BeforeEach
  public void setUp() {
    user = new User(MEMBER_EMAIL, PASSWORD, List.of(RoleType.ROLE_MEMBER.name()));

    강남역 = stationService.saveStation(new StationRequest("강남역"));
    역삼역 = stationService.saveStation(new StationRequest("역삼역"));
  }

  @Test
  void 즐겨찾기_생성() {
    FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 역삼역.getId());
    FavoriteResponse result = favoriteService.saveFavorite(favoriteRequest, user);

    assertThat(result.getSource().getName()).isEqualTo("강남역");
  }

  @Test
  void 즐겨찾기_생성_같은역_등록_에러() {
    FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 강남역.getId());
    assertThatThrownBy(() -> favoriteService.saveFavorite(favoriteRequest, user)).isInstanceOf(CustomException.class);
  }

  @Test
  void 즐겨찾기_조회() {
    FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 역삼역.getId());
    favoriteService.saveFavorite(favoriteRequest, user);

    List<FavoriteResponse> result = favoriteService.getFavorite(user);

    assertThat(result.get(0).getSource().getName()).isEqualTo("강남역");
  }
}
