package nextstep.favorite.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteSaveRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FavoriteController {

    @PostMapping("/favorites")
    public void saveFavorites(@RequestBody FavoriteSaveRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {

    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> results = List.of();
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public void deleteFavorites(@PathVariable Long favoriteId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
    }
}
