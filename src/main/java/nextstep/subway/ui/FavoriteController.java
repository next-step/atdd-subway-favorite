package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
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

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(
        @AuthenticationPrincipal final UserPrincipal userPrincipal,
        @RequestBody final FavoriteRequest favoriteRequest
    ) {
        Long favoriteId = favoriteService.createFavorite(userPrincipal.getUsername(), favoriteRequest);

        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
        @AuthenticationPrincipal final UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(favoriteService.findAll(userPrincipal.getUsername()));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(
        @AuthenticationPrincipal final UserPrincipal userPrincipal,
        @PathVariable final Long id
    ) {
        favoriteService.delete(userPrincipal.getUsername(), id);

        return ResponseEntity.noContent().build();
    }

}
