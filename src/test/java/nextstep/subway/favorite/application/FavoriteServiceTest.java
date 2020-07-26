package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    private Member member;
    private FavoriteService favoriteService;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private Station station;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
        station = new Station("test");

        memberRepository = mock(MemberRepository.class);
        stationRepository = mock(StationRepository.class);

        FavoriteRepository favoriteRepository = mock(FavoriteRepository.class);

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

        // when

        // then
    }

    @Test
    void findFavorites() {

        // when

        // then
    }
}