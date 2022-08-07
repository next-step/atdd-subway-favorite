package nextstep.subway.ui;

import nextstep.auth.secured.Secured;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
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

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@RequestBody FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteService.saveFavorite(favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(FavoriteResponse.of(favorite));
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites() {
        return ResponseEntity.ok().body(favoriteService.findFavorites());
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable("id") Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }

}
