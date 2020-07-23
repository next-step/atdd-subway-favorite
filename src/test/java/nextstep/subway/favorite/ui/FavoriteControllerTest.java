package nextstep.subway.favorite.ui;

import com.google.common.collect.Lists;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {
    private static final UserDetails LOGIN_USER = new UserDetails(1L, "email@email.com", "password", 20);

    private FavoriteController favoriteController;
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = mock(FavoriteService.class);
        favoriteController = new FavoriteController(favoriteService);
    }

    @DisplayName("createFavorite")
    @Test
    void createFavorite() {
        // given
        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        Favorite favorite = new Favorite(1L, LOGIN_USER.getId(), 1L, 2L);

        when(favoriteService.createFavorite(LOGIN_USER.getId(), request)).thenReturn(favorite);

        // when
        ResponseEntity response = favoriteController.createFavorite(LOGIN_USER, request);

        // then

        String path = response.getHeaders().getLocation().getPath();
        long responsedFavoriteId = Long.parseLong(path.split("favorites/")[1]);
        assertThat(responsedFavoriteId).isEqualTo(favorite.getId());
    }

    @DisplayName("getFavorites 즐겨찾기가 있는 경우")
    @Test
    void getFavoritesWhenFavoritesAreEmpty() {
        // given
        List<Station> stations = Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"));
        Favorite favorite = new Favorite(1L, LOGIN_USER.getId(), stations.get(0).getId(), stations.get(1).getId());

        when(favoriteService.findFavorites(LOGIN_USER.getId()))
                .thenReturn(Lists.newArrayList(FavoriteResponse.of(
                        favorite,
                        StationResponse.of(stations.get(0)),
                        StationResponse.of(stations.get(1))
                )));

        // when
        ResponseEntity<List<FavoriteResponse>> response = favoriteController.getFavorites(LOGIN_USER);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<FavoriteResponse> body = response.getBody();
        assertThat(body).hasSize(1);
        assertThat(body.get(0).getId()).isEqualTo(favorite.getId());
    }

    @DisplayName("getFavorites 즐겨찾기가 없는 경우")
    @Test
    void getFavoritesWhenFavoritesAreNotEmpty() {
        // given
        when(favoriteService.findFavorites(LOGIN_USER.getId())).thenReturn(Lists.newArrayList());

        // when
        ResponseEntity<List<FavoriteResponse>> response = favoriteController.getFavorites(LOGIN_USER);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<FavoriteResponse> body = response.getBody();
        assertThat(body).isEmpty();
    }

    @DisplayName("deleteFavorite 즐겨찾기가 있는 경우")
    @Test
    void deleteFavoriteWhenFavoriteFound() {
        // when
        ResponseEntity responseEntity = favoriteController.deleteFavorite(LOGIN_USER, 1L);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @DisplayName("deleteFavorite 즐겨찾기가 없는 경우")
    @Test
    void deleteFavoriteWhenFavoriteNotFound() {
        // given
        Long favoriteId = 1L;
        doThrow(EntityNotFoundException.class)
                .when(favoriteService).deleteFavorite(LOGIN_USER.getId(), favoriteId);

        // when
        ResponseEntity responseEntity = favoriteController.deleteFavorite(LOGIN_USER, favoriteId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}