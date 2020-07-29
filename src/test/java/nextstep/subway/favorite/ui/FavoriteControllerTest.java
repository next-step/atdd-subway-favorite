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
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;

public class FavoriteControllerTest {

    private LoginMember loginMember;
    private FavoriteService favoriteService;
    private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember(1L, "crongro@email.com", "ilovejavascript", 20);
        favoriteService = mock(FavoriteService.class);
        favoriteController = new FavoriteController(favoriteService);
    }

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void 즐겨찾기를_추가한다() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        ResponseEntity<Void> entity = favoriteController.createFavorite(loginMember, favoriteRequest);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(favoriteService).createFavorite(loginMember.getId(), favoriteRequest);
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void 즐겨찾기_목록을_조회한다() {
        ResponseEntity<List<FavoriteResponse>> entity = favoriteController.getFavorites(loginMember);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(favoriteService).findFavorites(loginMember.getId());
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void 즐겨찾기를_삭제한다() {
        ResponseEntity<Void> entity = favoriteController.deleteFavorite(loginMember, 1L);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(favoriteService).deleteFavorite(loginMember.getId(), 1L);
    }
}
