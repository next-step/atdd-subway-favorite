package nextstep.subway.favorite.ui;

import nextstep.subway.auth.application.UserDetail;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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
    public ResponseEntity<?> createFavorite(@RequestBody FavoriteRequest request, @AuthenticationPrincipal UserDetail userDetail) {
        favoriteService.createFavorite(request, userDetail.getId());
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserDetail currentUser) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(currentUser.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Long id, @AuthenticationPrincipal UserDetail currentUser) {
        favoriteService.deleteFavorite(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
