package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    private Member member;
    private FavoriteService favoriteService;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private Station station;
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
        station = new Station("test");

        memberRepository = mock(MemberRepository.class);
        stationRepository = mock(StationRepository.class);

        favoriteRepository = mock(FavoriteRepository.class);

        favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);
    }

    @Test
    void createFavorite() {
        Long memberId = 1L;
        FavoriteRequest favorite = new FavoriteRequest(1L, 2L);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(stationRepository.findById(anyLong())).thenReturn(Optional.of(station));

        // when
        favoriteService.createFavorite(memberId, favorite);

        // then
        verify(member).addFavorite(any());
    }

    @Test
    void createFavoriteWithInvalidUser() {
        Long memberId = 1L;
        FavoriteRequest favorite = new FavoriteRequest(1L, 2L);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(stationRepository.findById(anyLong())).thenReturn(Optional.of(station));

        // when & then
        Assertions.assertThatThrownBy(() -> favoriteService.createFavorite(memberId, favorite))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createFavoriteWithStation() {
        Long memberId = 1L;
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(stationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> favoriteService.createFavorite(memberId, favoriteRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteFavorite() {
        // given
        Long favoriteId = 1L;
        Favorite favorite = new Favorite();

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(favoriteRepository.findById(anyLong())).thenReturn(Optional.of(favorite));

        // when
        favoriteService.deleteFavorite(member.getId(), favoriteId);

        // then
        verify(member).deleteFavorite(any());
    }

    @Test
    void findFavorites() {
        // given
        Long sourceStationId = 1L;
        Long targetStationId = 2L;
        Long memberId = 1L;
        Member member = new Member();
        Favorite favorite = new Favorite(sourceStationId, targetStationId);
        member.addFavorite(favorite);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(favoriteRepository.findById(anyLong())).thenReturn(Optional.of(favorite));
        member.addFavorite(favorite);

        // when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(memberId);

        // then
        assertThat(favorites).isNotEmpty();
        FavoriteResponse favoriteResponse = favorites.get(0);
        assertThat(favoriteResponse.getSource()).isEqualTo(sourceStationId);
        assertThat(favoriteResponse.getTarget()).isEqualTo(targetStationId);
    }
}