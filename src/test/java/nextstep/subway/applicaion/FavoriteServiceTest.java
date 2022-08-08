package nextstep.subway.applicaion;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    private FavoriteService favoriteService;
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        stationRepository = mock(StationRepository.class);

        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
    }

    @DisplayName("즐겨찾기 등록")
    @Test
    void createFavorite() {
        var userId = 123L;
        var source = createStation(1L, "출발역");
        var target = createStation(2L, "도착역");
        when(stationRepository.findById(source.getId()))
                .thenReturn(Optional.of(source));
        when(stationRepository.findById(target.getId()))
                .thenReturn(Optional.of(target));

        favoriteService.createFavorite(userId, source.getId(), target.getId());

        var captor = ArgumentCaptor.forClass(Favorite.class);
        assertAll(
                () -> verify(favoriteRepository, times(1)).save(captor.capture()),
                () -> assertThat(captor.getValue().getUserId()).isEqualTo(userId),
                () -> assertThat(captor.getValue().getSource()).isEqualTo(source),
                () -> assertThat(captor.getValue().getTarget()).isEqualTo(target)
        );
    }

    @DisplayName("동일한 역에 대하여 즐겨찾기 등록 실패")
    @Test
    void createFavoriteFailsForSameStations() {
        var userId = 123L;
        var source = createStation(1L, "출발역");

        assertThrows(IllegalArgumentException.class,
                () -> favoriteService.createFavorite(userId, source.getId(), source.getId()));
    }

    @DisplayName("존재하지 않는 역에 대하여 즐겨찾기 등록 실패")
    @Test
    void createFavoriteFailsForStationNotExist() {
        var userId = 123L;
        var source = createStation(1L, "출발역");
        var target = createStation(2L, "도착역");
        when(stationRepository.findById(source.getId()))
                .thenReturn(Optional.of(source));
        when(stationRepository.findById(target.getId()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> favoriteService.createFavorite(userId, source.getId(), target.getId()));
    }

    private Station createStation(Long id, String name) {
        var station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}