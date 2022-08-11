package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.auth.user.User;
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

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @Secured("ROLE_MEMBER")
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal User user, @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse response = favoriteService.createFavorite(user.getEmail(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
    }

    @GetMapping
    @Secured("ROLE_MEMBER")
    public ResponseEntity<List<FavoriteResponse>> getAllFavorites(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(favoriteService.getAllFavorite(user.getEmail()));
    }

    @GetMapping("/{id}")
    @Secured("ROLE_MEMBER")
    public ResponseEntity<FavoriteResponse> getFavorite(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok().body(favoriteService.getFavorite(id, user.getEmail()));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_MEMBER")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal User user, @PathVariable Long id) {
        favoriteService.deleteFavorite(id, user.getEmail());
        return ResponseEntity.noContent().build();
    }
}
