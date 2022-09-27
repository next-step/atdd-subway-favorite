package nextstep.subway.unit;

import nextstep.subway.applicaion.FavoritesService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoritesRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.subway.unit.support.StationMockFactory.station;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class FavoritesServiceTest {
    @Mock
    private StationService stationService;
    @Mock
    private FavoritesRepository favoritesRepository;
    @InjectMocks
    private FavoritesService favoritesService;

    @Test
    void createFavorite() {
        // given
        Station source = station(1L);
        Station target = station(2L);
        FavoriteCreateRequest request = new FavoriteCreateRequest(source.getId(), target.getId());
        given(stationService.findById(source.getId())).willReturn(source);
        given(stationService.findById(target.getId())).willReturn(target);

        // when
        FavoriteResponse response = favoritesService.createFavorite(request);

        // then
        assertAll(
            () -> assertThat(response.getSource().getId()).isEqualTo(source.getId()),
            () -> assertThat(response.getSource().getName()).isEqualTo(source.getName()),
            () -> assertThat(response.getTarget().getId()).isEqualTo(target.getId()),
            () -> assertThat(response.getTarget().getName()).isEqualTo(target.getName())
                 );
        verify(favoritesRepository).save(any(Favorite.class));
    }
}