package nextstep.favorite.presentation;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.dto.FavoriteCreateRequest;
import nextstep.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        Long favoriteId = favoriteService.saveFavorite(userPrincipal.getUsername(), favoriteCreateRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(favoriteService.findAllFavorites(userPrincipal.getUsername()));
    }
}
