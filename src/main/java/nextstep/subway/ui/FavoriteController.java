package nextstep.subway.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
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
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest favoriteRequest,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Favorite favorite = favoriteService.createFavorite(favoriteRequest, userPrincipal.getUsername());
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(userPrincipal.getUsername());
        return ResponseEntity.ok(favoriteResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        favoriteService.deleteFavorite(id, userPrincipal.getUsername());
        return ResponseEntity.noContent().build();
    }
}