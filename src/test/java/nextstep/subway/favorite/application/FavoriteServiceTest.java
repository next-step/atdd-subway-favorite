package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    private Member member;
    private FavoriteService favoriteService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);

        memberRepository = mock(MemberRepository.class);
        FavoriteRepository favoriteRepository = mock(FavoriteRepository.class);
        StationRepository stationRepository = mock(StationRepository.class);

        favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);
    }

    @Test
    void createFavorite() {
        Long memberId = 1L;
        FavoriteRequest favorite = new FavoriteRequest(1L, 2L);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

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

        // when & then
        Assertions.assertThatThrownBy(() -> favoriteService.createFavorite(memberId, favorite))
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