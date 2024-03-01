package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteCreateRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.auth.domain.LoginMember;
import nextstep.auth.config.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(
        @AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteCreateRequest request
    ) {
        return ResponseEntity
                .created(URI.create("/favorites/" + favoriteService.createFavorite(loginMember, request)))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);
        return ResponseEntity.ok().body(favorites);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteFavorite(
        @AuthenticationPrincipal LoginMember loginMember,
        @PathVariable Long id
    ) {
        favoriteService.deleteFavorite(loginMember, id);
    }
}
