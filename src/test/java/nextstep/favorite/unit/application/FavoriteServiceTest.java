package nextstep.favorite.unit.application;

import static nextstep.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;
import nextstep.favorite.application.FavoriteMapper;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.FavoriteNotFoundException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.exception.AuthorizationException;
import nextstep.subway.station.domain.Station;
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

  private final Member member = aMember().build();
  private final LoginMember loginMember = new LoginMember(member.getEmail());

  @DisplayName("즐겨찾기를 저장한다.")
  @Test
  void createFavorite() {
    FavoriteRequest request = FavoriteRequest.of(교대역().getId(), 양재역().getId());
    given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);

    favoriteService.createFavorite(request, loginMember);

    then(favoriteRepository).should().save(any(Favorite.class));
  }

  @DisplayName("즐겨찾기 목록을 조회한다.")
  @Test
  void findFavorites() {
    given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);

    favoriteService.findFavorites(loginMember);

    then(favoriteRepository).should().findAllByMemberId(member.getId());
  }

  @DisplayName("즐겨찾기를 삭제한다.")
  @Test
  void deleteFavorite() {
    given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);
    given(favoriteRepository.findById(1L)).willReturn(Optional.of(aFavorite().build()));

    favoriteService.deleteFavorite(1L, loginMember);

    then(favoriteRepository).should().deleteById(1L);
  }

  @DisplayName("존재하지 않는 즐겨찾기를 삭제하려 하면 예외가 던져진다.")
  @Test
  void deleteFavoriteNotFound() {
    given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);
    given(favoriteRepository.findById(99L)).willReturn(Optional.empty());

    assertThatExceptionOfType(FavoriteNotFoundException.class)
        .isThrownBy(() -> favoriteService.deleteFavorite(99L, loginMember));
  }

  @DisplayName("다른 사용자의 즐겨찾기를 삭제하려 하면 예외 처리된다.")
  @Test
  void deleteFavoriteBelongingToAnotherUser() {
    Station 교대역 = 교대역();
    Station 양재역 = 양재역();
    long otherMemberId = 99L;
    long favoriteId = 1L;
    Favorite favorite =
        Favorite.builder()
            .id(favoriteId)
            .sourceStationId(교대역.getId())
            .targetStationId(양재역.getId())
            .memberId(otherMemberId)
            .build();
    given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);
    given(favoriteRepository.findById(favoriteId)).willReturn(Optional.of(favorite));

    assertThatExceptionOfType(AuthorizationException.class)
        .isThrownBy(() -> favoriteService.deleteFavorite(favoriteId, loginMember));
  }
}
