package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.common.exception.CustomException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

  @Mock
  private MemberService memberService;
  @Mock
  private StationService stationService;
  @Mock
  private FavoriteRepository favoriteRepository;

  @InjectMocks
  private FavoriteService favoriteService;

  static final String MEMBER_EMAIL = "test@email.com";
  static final String PASSWORD = "password";

  private Station 강남역;
  private Station 역삼역;

  private Member member;
  private Favorite favorite;
  private FavoriteRequest favoriteRequest;

  @BeforeEach
  public void setUp() {
    강남역 = new Station(10L, "강남역");
    역삼역 = new Station(11L, "역삼역");

    member = new Member(MEMBER_EMAIL, PASSWORD, 20);
    favorite = new Favorite(강남역, 역삼역, member.getId());
    favoriteRequest = new FavoriteRequest(강남역.getId(), 역삼역.getId());
  }

  @Test
  void 즐겨찾기_생성() {
    when(stationService.findById(강남역.getId())).thenReturn(강남역);
    when(stationService.findById(역삼역.getId())).thenReturn(역삼역);

    when(memberService.findByEmail(MEMBER_EMAIL)).thenReturn(member);
    when(favoriteRepository.save(any())).thenReturn(favorite);

    FavoriteResponse favoriteResponse = favoriteService.saveFavorite(favoriteRequest, MEMBER_EMAIL);

    assertThat(favoriteResponse.getSource().getName()).isEqualTo("강남역");
    assertThat(favoriteResponse.getTarget().getName()).isEqualTo("역삼역");
  }

  @Test
  void 즐겨찾기_생성_같은역_등록_에러() {
    when(stationService.findById(강남역.getId())).thenReturn(강남역);

    FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 강남역.getId());
    assertThatThrownBy(() -> favoriteService.saveFavorite(favoriteRequest, MEMBER_EMAIL)).isInstanceOf(CustomException.class);
  }

  @Test
  void 즐겨찾기_조회() {
    when(memberService.findByEmail(MEMBER_EMAIL)).thenReturn(member);
    when(favoriteRepository.findByMemberId(any())).thenReturn(List.of(favorite));

    List<FavoriteResponse> favorites = favoriteService.getFavorite(MEMBER_EMAIL);

    assertThat(favorites.get(0).getSource().getName()).isEqualTo("강남역");
  }
}
