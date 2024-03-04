package nextstep.favorite.ui;

import nextstep.auth.application.UserDetails;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
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
    public ResponseEntity<FavoriteResponse> createFavorite(@RequestBody FavoriteRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        FavoriteResponse favorite = favoriteService.createFavorite(request, userDetails);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(userDetails);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
