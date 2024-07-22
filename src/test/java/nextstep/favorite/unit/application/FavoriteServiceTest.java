package nextstep.favorite.unit.application;

import static nextstep.Fixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import nextstep.favorite.application.FavoriteMapper;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
  @Mock private FavoriteRepository favoriteRepository;
  @Mock private MemberService memberService;
  @Mock private FavoriteMapper favoriteMapper;
  @InjectMocks private FavoriteService favoriteService;

  @DisplayName("즐겨찾기를 저장한다.")
  @Test
  void createFavoriteShouldPersist() {
    Member member = aMember().build();
    FavoriteRequest request = FavoriteRequest.of(교대역().getId(), 양재역().getId());
    given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);

    favoriteService.createFavorite(request, new LoginMember(member.getEmail()));

    then(favoriteRepository).should().save(any(Favorite.class));
  }

  @DisplayName("즐겨찾기 목록을 조회한다.")
  @Test
  void findFavorites() {
    favoriteService.findFavorites();

    then(favoriteRepository).should().findAll();
  }
}
