package nextstep.subway.ui;

import nextstep.auth.authentication.User;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@RequestBody FavoriteRequest favoriteRequest, @AuthenticationPrincipal User user) {
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(favoriteRequest, user.getId());
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id, @AuthenticationPrincipal User user) {
        favoriteService.deleteFavorite(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(favoriteService.getFavorites(user.getId()));
    }
}
