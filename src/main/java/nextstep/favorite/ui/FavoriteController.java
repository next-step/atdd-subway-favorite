package nextstep.favorite.ui;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.auth.principal.LoginMember;
import nextstep.auth.principal.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                         @RequestBody FavoriteRequest request) {
        Long favoriteId = favoriteService.createFavorite(request, loginMember.getId());

        return ResponseEntity
                .created(URI.create("/favorites/" + favoriteId))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {

        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                         @PathVariable Long id) {

        favoriteService.deleteFavorite(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
