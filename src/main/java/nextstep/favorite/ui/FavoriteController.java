package nextstep.favorite.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteSaveRequest;
import nextstep.favorite.application.dto.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> saveFavorites(@RequestBody FavoriteSaveRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        FavoriteResponse saved = favoriteService.saveFavorite(request, userPrincipal.getUsername());
        return ResponseEntity.created(URI.create("/favorites/" + saved.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> results = favoriteService.findFavoritesByEmail(userPrincipal.getUsername());
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavorites(@PathVariable Long favoriteId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        favoriteService.deleteById(favoriteId, userPrincipal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
