package nextstep.favorite.ui;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> registerFavorite(@RequestBody FavoriteRequest favoriteRequest) {
        Long id = favoriteService.registerFavorite(favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites() {
        return ResponseEntity.ok().body(favoriteService.getFavorites());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        boolean isDeleted = favoriteService.deleteFavorite(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.internalServerError().build();
    }
}
