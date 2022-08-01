package nextstep.favorite.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.userdetails.User;
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

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> saveFavorite(
            @AuthenticationPrincipal final User user,
            @RequestBody final FavoriteRequest favoriteRequest) {

        final FavoriteResponse response = favoriteService.saveFavorite(user.getEmail(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal final User user) {
        final List<FavoriteResponse> response = favoriteService.findFavorites(user.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> getFavorite(
            @AuthenticationPrincipal final User user,
            @PathVariable final Long id) {
        final FavoriteResponse response = favoriteService.findFavorite(user.getEmail(), id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(
            @AuthenticationPrincipal final User user,
            @PathVariable final Long id) {

        favoriteService.deleteFavorite(user.getEmail(), id);
        return ResponseEntity.noContent().build();
    }

}

