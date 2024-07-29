package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteCommandService;
import nextstep.favorite.application.FavoriteQueryService;
import nextstep.favorite.payload.FavoriteRequest;
import nextstep.favorite.payload.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteCommandService favoriteCommandService;

    private final FavoriteQueryService favoriteQueryService;

    public FavoriteController(final FavoriteCommandService favoriteCommandService, final FavoriteQueryService favoriteQueryService) {
        this.favoriteCommandService = favoriteCommandService;
        this.favoriteQueryService = favoriteQueryService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        Long id = favoriteCommandService.createFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + id)).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteQueryService.findFavorites(loginMember.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id) {
        favoriteCommandService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
