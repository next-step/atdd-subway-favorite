package nextstep.favorite.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.request.FavoriteCreateRequest;
import nextstep.favorite.application.response.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(
            @RequestBody FavoriteCreateRequest favoriteCreateRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Favorite favorite = favoriteService.createFavorite(userPrincipal, favoriteCreateRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(userPrincipal);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(
            @PathVariable Long favoriteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        favoriteService.deleteFavorite(favoriteId, userPrincipal);
        return ResponseEntity.noContent().build();
    }

}
