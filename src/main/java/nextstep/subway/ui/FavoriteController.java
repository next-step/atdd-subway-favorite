package nextstep.subway.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.application.FavoriteService;
import nextstep.subway.application.dto.favorite.FavoriteRequest;
import nextstep.subway.application.dto.favorite.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) { this.favoriteService = favoriteService; }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.createFavorite(userPrincipal.getUsername(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> response = favoriteService.findFavorite(userPrincipal.getUsername());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}