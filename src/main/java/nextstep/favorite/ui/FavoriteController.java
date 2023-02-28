package nextstep.favorite.ui;

import nextstep.auth.AuthenticationUser;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
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
    public ResponseEntity<Void> createFavorite(@AuthenticationUser final String email,
                                               @RequestBody final FavoriteRequest request) {
        final Favorite favorite = favoriteService.saveFavorite(email, request);
        return ResponseEntity.created(URI.create("/favorite/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showsFavorites(@AuthenticationUser final String email) {
        return ResponseEntity.ok(favoriteService.showFavorites(email));
    }
}
