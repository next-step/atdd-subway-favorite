package nextstep.subway.ui;

import nextstep.auth.User;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Secured({"ROLE_ADMIN","ROLE_MEMBER"})
    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal User user, @RequestBody FavoriteRequest request) {
        Long favoriteId = favoriteService.createFavorite(Long.valueOf(user.getEmail()), request.getSource(), request.getTarget());
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @Secured({"ROLE_ADMIN","ROLE_MEMBER"})
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal User user, @PathVariable Long id) {
        favoriteService.deleteFavorite(Long.valueOf(user.getEmail()), id);
        return ResponseEntity.noContent().build();
    }

    @Secured({"ROLE_ADMIN","ROLE_MEMBER"})
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal User user) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(Long.valueOf(user.getEmail()));
        return ResponseEntity.ok(favorites);
    }
}
