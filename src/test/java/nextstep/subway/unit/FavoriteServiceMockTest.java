package nextstep.subway.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.NotFoundFavoriteException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    Member member;
    Station 출발역;
    Station 도착역;
    Favorite favorite;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "email", "password", 20);
        출발역 = new Station(2L, "출발역");
        도착역 = new Station(3L, "도착역");
        favorite = new Favorite(4L, member.getId(), 출발역, 도착역);
    }

    @DisplayName("즐겨찾기를 신규로 등록한다")
    @Test
    public void create_favorite_success() {
        // given
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(stationService.findById(출발역.getId())).willReturn(출발역);
        given(stationService.findById(도착역.getId())).willReturn(도착역);
        given(favoriteRepository.save(any())).willReturn(favorite);

        // when
        Long findFavorite = favoriteService.createFavorite(member.getEmail(), 출발역.getId(), 도착역.getId());

        // then
        assertThat(findFavorite).isEqualTo(4L);
    }

    @DisplayName("즐겨찾기 목록을 성공적으로 반환받는다")
    @Test
    public void find_all_favorites_success() {
        // given
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(favoriteRepository.findAllByMemberId(any())).willReturn(List.of(favorite));

        // when
        List<FavoriteResponse> favorites = favoriteService.findAllFavorites(member.getEmail());

        // then
        assertAll(
                () -> assertThat(favorites.get(0).getSource().getName()).isEqualTo("출발역"),
                () -> assertThat(favorites.get(0).getTarget().getName()).isEqualTo("도착역")
        );
    }

    @DisplayName("즐겨찾기를 성공적으로 삭제한다")
    @Test
    public void delete_favorite_success() {
        // given
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(favoriteRepository.existsByMemberIdAndId(anyLong(), anyLong())).willReturn(true);

        // when
        favoriteService.deleteById(member.getEmail(), favorite.getId());
        
        // then
        verify(favoriteRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("존재하지 않는 즐겨찾기 번호로 삭제시 실패한다")
    @Test
    public void delete_favorite_fail() {
        // given
        Long 비정상적인_즐겨찾기_id = 7777L;
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(favoriteRepository.existsByMemberIdAndId(anyLong(), anyLong())).willReturn(false);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> favoriteService.deleteById(member.getEmail(), 비정상적인_즐겨찾기_id);

        // then
        assertThatThrownBy(actual)
                .isInstanceOf(NotFoundFavoriteException.class)
                .hasMessage(NotFoundFavoriteException.MESSAGE);
    }
}
