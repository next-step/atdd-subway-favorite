package nextstep.favorite.ui;

import nextstep.auth.domain.UserDetail;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteCreateResponse;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteCreateResponse> createFavorite(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody FavoriteRequest request) {
        FavoriteCreateResponse favoriteResponse = favoriteService.createFavorite(userDetail, request);

        return ResponseEntity
                .created(URI.create("/favorites/" + favoriteResponse.getId()))
                .body(favoriteResponse);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            @AuthenticationPrincipal UserDetail userDetail
    ) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(userDetail);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable Long id
    ) {
        favoriteService.deleteFavorite(userDetail, id);
        return ResponseEntity.noContent().build();
    }
}
