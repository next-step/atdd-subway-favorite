package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private StationService stationService;

    private static final long memberId = 1L;
    private static final long source = 2L;
    private static final long target = 3L;

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

        doReturn(station(source))
                .when(stationService)
                .findById(source);

        doReturn(station(target))
                .when(stationService)
                .findById(target);

        // when
        final FavoriteResponse result = favoriteService.saveFavorite(1L, favoriteRequest);

        // then
        assertThat(result).isNotNull();

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    private Station station(final long id) {
        return new Station(id, "name", LocalDateTime.now(), LocalDateTime.now());
    }

    private FavoriteRequest favoriteRequest() {
        return new FavoriteRequest(source, target);
    }

    private Favorite favorite() {
        return new Favorite(memberId, source, target);
    }
}