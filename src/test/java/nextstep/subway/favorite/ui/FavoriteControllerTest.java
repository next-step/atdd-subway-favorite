package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class FavoriteControllerTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final long ID = 1L;

    private LoginMember loginMember;
    private FavoriteService favoriteService;
    private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        favoriteService = mock(FavoriteService.class);
        favoriteController = new FavoriteController(favoriteService);
    }

    @Test
    void create() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(anyLong(), anyLong());

        // when
        ResponseEntity entity = favoriteController.createFavorite(loginMember, favoriteRequest);

        // then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(favoriteService).createFavorite(loginMember.getId(), favoriteRequest);
    }

    @Test
    void get() {
        // when
        ResponseEntity entity = favoriteController.getFavorites(loginMember);

        // then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(favoriteService).findFavorites(loginMember.getId());
    }


    @Test
    void delete() {
        // when
        ResponseEntity entity = favoriteController.deleteFavorite(loginMember, ID);

        // then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(favoriteService).deleteFavorite(loginMember.getId(), ID);
    }
}
