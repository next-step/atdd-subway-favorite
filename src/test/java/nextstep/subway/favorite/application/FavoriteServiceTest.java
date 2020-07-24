package nextstep.subway.favorite.application;

import com.google.common.collect.Lists;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {
    private static final Long MEMBER_ID = 1L;

    private FavoriteService favoriteService;
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        stationRepository = mock(StationRepository.class);
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
    }

    @DisplayName("createFavorite")
    @Test
    void createFavorite() {
        // given
        Favorite favorite = new Favorite(1L, MEMBER_ID, 1L, 2L);
        when(favoriteRepository.save(any())).thenReturn(favorite);
        FavoriteRequest request = new FavoriteRequest(1L, 2L);

        // when
        Favorite createdFavorite = favoriteService.createFavorite(MEMBER_ID, request);

        // then
        verify(favoriteRepository).save(any());
        assertThat(createdFavorite.getId()).isEqualTo(favorite.getId());
    }

    @DisplayName("findFavorites 즐겨찾기가 있는 경우")
    @Test
    void getFavoritesWhenFavoritesAreEmpty() {
        // given
        List<Station> stations = Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"));
        Favorite favorite = new Favorite(1L, MEMBER_ID, stations.get(0).getId(), stations.get(1).getId());

        when(favoriteRepository.findAllByMemberId(MEMBER_ID)).thenReturn(Lists.newArrayList(favorite));
        when(stationRepository.findAllById(stations.stream().map(Station::getId).collect(Collectors.toSet())))
                .thenReturn(stations);

        // when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(MEMBER_ID);

        // then
        verify(favoriteRepository).findAllByMemberId(MEMBER_ID);
        verify(stationRepository).findAllById(stations.stream().map(Station::getId).collect(Collectors.toSet()));

        assertThat(favorites).hasSize(1);
        assertThat(favorites.get(0).getId()).isEqualTo(favorite.getId());

    }

    @DisplayName("findFavorites 즐겨찾기가 없는 경우")
    @Test
    void getFavoritesWhenFavoritesAreNotEmpty() {
        // given
        when(favoriteRepository.findAllByMemberId(MEMBER_ID)).thenReturn(Lists.newArrayList());

        // when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(MEMBER_ID);

        // then
        verify(favoriteRepository).findAllByMemberId(MEMBER_ID);
        assertThat(favorites).isEmpty();
    }

    @DisplayName("deleteFavorite 즐겨찾기가 있는 경우: 정상 동작")
    @Test
    void deleteFavoriteWhenFavoriteFound() {
        // given
        Long favoriteId = 1L;
        when(favoriteRepository.existsByIdAndMemberId(favoriteId, MEMBER_ID)).thenReturn(true);

        // when
        favoriteService.deleteFavorite(MEMBER_ID, favoriteId);

        // then
        verify(favoriteRepository).existsByIdAndMemberId(favoriteId, MEMBER_ID);
    }

    @DisplayName("deleteFavorite 즐겨찾기가 없는 경우: EntityNotFoundException")
    @Test
    void deleteFavoriteWhenFavoriteNotFound() {
        // given
        Long favoriteId = 1L;
        when(favoriteRepository.existsByIdAndMemberId(favoriteId, MEMBER_ID)).thenReturn(false);

        // when then
        assertThrows(EntityNotFoundException.class, () -> favoriteService.deleteFavorite(MEMBER_ID, favoriteId));
        verify(favoriteRepository).existsByIdAndMemberId(favoriteId, MEMBER_ID);
    }
}