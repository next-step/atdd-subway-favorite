package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static nextstep.favorite.FavoriteUnitSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Test
    void 즐겨찾기추가() {
        // given
        final FavoriteRequest favoriteRequest = favoriteRequest();
        final Favorite favorite = favorite();

        doReturn(favorite)
                .when(favoriteRepository)
                .save(any(Favorite.class));

        doReturn(new MemberResponse(memberId, email, 0))
                .when(memberService)
                .findMember(email);

        doReturn(station(source))
                .when(stationService)
                .findById(source);

        doReturn(station(target))
                .when(stationService)
                .findById(target);

        // when
        final FavoriteResponse result = favoriteService.saveFavorite(email, favoriteRequest);

        // then
        assertThat(result).isNotNull();

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    void 즐겨찾기조회성공() {
        final Long id = 1L;
        final Favorite favorite = favorite();

        doReturn(new MemberResponse(memberId, email, 0))
                .when(memberService)
                .findMember(email);

        doReturn(List.of(favorite))
                .when(favoriteRepository)
                .findAllByMemberId(id);

        doReturn(station(source))
                .when(stationService)
                .findById(source);

        doReturn(station(target))
                .when(stationService)
                .findById(target);

        final List<FavoriteResponse> result = favoriteService.findFavorites(email);
        assertThat(result).hasSize(1);
    }

    private Station station(final long id) {
        return new Station(id, "name", LocalDateTime.now(), LocalDateTime.now());
    }

}