package nextstep.subway.favorite.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;

public class FavoriteControllerTest {

    private LoginMember loginMember;
    private FavoriteService favoriteService;
    private FavoriteController favoriteController;
    private Long SOURCE;
    private Long TARGET;
    private Long ID;
    private String EMAIL;
    private String PASSWORD;
    private Integer AGE;

    @BeforeEach
    void setUp() {
        SOURCE = 1L;
        TARGET = 2L;
        ID = 1L;
        EMAIL = "crongro@gmail.com";
        PASSWORD = "ilovejavascript";
        AGE = 20;
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        favoriteService = mock(FavoriteService.class);
        favoriteController = new FavoriteController(favoriteService);
    }

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void 즐겨찾기를_추가한다() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(SOURCE, TARGET);
        Favorite favorite = new Favorite(ID, loginMember.getId(), SOURCE, TARGET);
        when(favoriteService.createFavorite(loginMember.getId(), favoriteRequest)).thenReturn(favorite);

        // when
        ResponseEntity<Void> entity = favoriteController.createFavorite(loginMember, favoriteRequest);

        // then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(favoriteService).createFavorite(loginMember.getId(), favoriteRequest);
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void 즐겨찾기_목록을_조회한다() {
        // when
        ResponseEntity<List<FavoriteResponse>> entity = favoriteController.getFavorites(loginMember);

        // then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(favoriteService).findFavorites(loginMember.getId());
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void 즐겨찾기를_삭제한다() {
        // when
        ResponseEntity<Void> entity = favoriteController.deleteFavorite(loginMember, 1L);

        // then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(favoriteService).deleteFavorite(loginMember.getId(), 1L);
    }
}
