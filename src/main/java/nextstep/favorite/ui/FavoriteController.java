package nextstep.favorite.ui;

import nextstep.auth.domain.UserDetails;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
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
    public ResponseEntity<Void> createFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody FavoriteRequest request) {
        Favorite result = favoriteService.createFavorite(request, userDetails.getId());
        return ResponseEntity
                .created(URI.create("/favorites/" + result.getId()))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(userDetails.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        favoriteService.deleteFavorite(userDetails.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
