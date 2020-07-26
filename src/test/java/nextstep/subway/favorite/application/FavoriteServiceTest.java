package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    @Test
    void createFavorite() {
        Member member = mock(Member.class);
        FavoriteRepository favoriteRepository = mock(FavoriteRepository.class);
        MemberRepository memberRepository = mock(MemberRepository.class);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        StationRepository stationRepository = mock(StationRepository.class);
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);

        Long memberId = 1L;
        FavoriteRequest favorite = new FavoriteRequest(1L, 2L);

        // when
        favoriteService.createFavorite(memberId, favorite);

        // then
        verify(member).addFavorite(any());
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