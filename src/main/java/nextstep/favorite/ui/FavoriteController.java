package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.auth.ui.UserPrincipal;
import nextstep.auth.ui.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal final UserPrincipal userPrincipal, @RequestBody final FavoriteRequest request) {
        request.validate();
        final FavoriteResponse response = favoriteService.createFavorite(userPrincipal, request);
        return ResponseEntity
                .created(URI.create("/favorites/" + response.getId()))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal final UserPrincipal userPrincipal) {
        final List<FavoriteResponse> favorites = favoriteService.findFavorites(userPrincipal);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable final Long id, @AuthenticationPrincipal final UserPrincipal userPrincipal) {
        favoriteService.deleteFavorite(userPrincipal, id);
        return ResponseEntity.noContent().build();
    }
}
