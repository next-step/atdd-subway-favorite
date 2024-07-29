package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.application.dto.FavoriteRequest;
import nextstep.subway.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request) {
        favoriteService.createFavorite(request);
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites() {
        List<FavoriteResponse> favorites = favoriteService.findFavorites();
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
