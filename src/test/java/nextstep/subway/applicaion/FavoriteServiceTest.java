package nextstep.subway.applicaion;

import nextstep.subway.applicaion.favorite.FavoriteService;
import nextstep.subway.applicaion.favorite.exception.InvalidFavoriteOwnerException;
import nextstep.subway.applicaion.favorite.exception.InvalidFavoriteStationException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    private FavoriteService favoriteService;
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    private final Long USER_ID = 123L;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        stationRepository = mock(StationRepository.class);

        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
    }

    @DisplayName("즐겨찾기 등록")
    @Test
    void createFavorite() {
        var source = createStation(1L, "출발역");
        var target = createStation(2L, "도착역");
        when(stationRepository.findById(source.getId()))
                .thenReturn(Optional.of(source));
        when(stationRepository.findById(target.getId()))
                .thenReturn(Optional.of(target));
        when(favoriteRepository.save(any(Favorite.class)))
                .thenAnswer(args -> args.getArgument(0));

        favoriteService.createFavorite(USER_ID, source.getId(), target.getId());

        var captor = ArgumentCaptor.forClass(Favorite.class);
        assertAll(
                () -> verify(favoriteRepository, times(1)).save(captor.capture()),
                () -> assertThat(captor.getValue().getUserId()).isEqualTo(USER_ID),
                () -> assertThat(captor.getValue().getSource()).isEqualTo(source),
                () -> assertThat(captor.getValue().getTarget()).isEqualTo(target)
        );
    }

    @DisplayName("동일한 역에 대하여 즐겨찾기 등록 실패")
    @Test
    void createFavoriteFailsForSameStations() {
        var source = createStation(1L, "출발역");

        assertThrows(InvalidFavoriteStationException.class,
                () -> favoriteService.createFavorite(USER_ID, source.getId(), source.getId()));
    }

    @DisplayName("존재하지 않는 역에 대하여 즐겨찾기 등록 실패")
    @Test
    void createFavoriteFailsForStationNotExist() {
        var source = createStation(1L, "출발역");
        var target = createStation(2L, "도착역");
        when(stationRepository.findById(source.getId()))
                .thenReturn(Optional.of(source));
        when(stationRepository.findById(target.getId()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidFavoriteStationException.class,
                () -> favoriteService.createFavorite(USER_ID, source.getId(), target.getId()));
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {
        var favorites = List.of(
                new Favorite(1L, USER_ID, createStation(1L, "강남역"), createStation(2L, "양재역"))
        );
        when(favoriteRepository.findAllByUserId(USER_ID))
                .thenReturn(favorites);

        var result = favoriteService.getFavorites(USER_ID);

        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getId()).isEqualTo(favorites.get(0).getId()),
                () -> assertThat(result.get(0).getTarget().getId()).isEqualTo(favorites.get(0).getTarget().getId()),
                () -> assertThat(result.get(0).getSource().getId()).isEqualTo(favorites.get(0).getSource().getId())
        );
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void removeFavorite() {
        var favorite = new Favorite(1L, 123L, createStation(1L, "강남역"), createStation(2L, "양재역"));
        when(favoriteRepository.findById(favorite.getId()))
                .thenReturn(Optional.of(favorite));

        favoriteService.removeFavorite(favorite.getUserId(), favorite.getId());

        verify(favoriteRepository, times(1)).deleteById(favorite.getId());
    }

    @DisplayName("유저가 소유하지 않은 즐겨찾기 삭제 시도시 예외 발생")
    @Test
    void removeFavoriteFailsWhenUserIsNotMatch() {
        var userId = 987L;
        var favorite = new Favorite(1L, USER_ID, createStation(1L, "강남역"), createStation(2L, "양재역"));
        when(favoriteRepository.findById(favorite.getId()))
                .thenReturn(Optional.of(favorite));

        assertThrows(InvalidFavoriteOwnerException.class, () -> favoriteService.removeFavorite(userId, favorite.getId()));
    }

    @DisplayName("존재하지 않는 즐겨찾기 삭제시 예외 발생")
    @Test
    void removeFavoriteFailsForIdNotExist() {
        var favoriteIdNotExist = 123123L;
        when(favoriteRepository.findById(favoriteIdNotExist))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> favoriteService.removeFavorite(USER_ID, favoriteIdNotExist));
    }

    private Station createStation(Long id, String name) {
        var station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}