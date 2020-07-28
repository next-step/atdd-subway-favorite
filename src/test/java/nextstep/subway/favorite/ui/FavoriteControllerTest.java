package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class FavoriteControllerTest {

    @Test
    void create() {
        LoginMember loginMember = new LoginMember(1L, "email@email.com", "password", 20);
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);

        FavoriteService favoriteService = mock(FavoriteService.class);
        FavoriteController favoriteController = new FavoriteController(favoriteService);

        ResponseEntity entity = favoriteController.createFavorite(loginMember, favoriteRequest);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(favoriteService).createFavorite(loginMember.getId(), favoriteRequest);
    }


    @Test
    void get() {
        LoginMember loginMember = new LoginMember(1L, "email@email.com", "password", 20);

        FavoriteService favoriteService = mock(FavoriteService.class);
        FavoriteController favoriteController = new FavoriteController(favoriteService);

        ResponseEntity entity = favoriteController.getFavorites(loginMember);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(favoriteService).findFavorites(loginMember.getId());
    }


    @Test
    void delete() {
        LoginMember loginMember = new LoginMember(1L, "email@email.com", "password", 20);
        FavoriteService favoriteService = mock(FavoriteService.class);
        FavoriteController favoriteController = new FavoriteController(favoriteService);

        ResponseEntity entity = favoriteController.deleteFavorite(loginMember, 1L);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(favoriteService).deleteFavorite(loginMember.getId(), 1L);
    }
}
