package nextstep.favorite.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.CreateFavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody CreateFavoriteRequest createFavoriteRequest
    ) {
        FavoriteResponse response = favoriteService.createFavorite(userPrincipal, createFavoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites();
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long id
    ) {
        favoriteService.deleteFavorite(userPrincipal, id);
        return ResponseEntity.noContent().build();
    }
}
