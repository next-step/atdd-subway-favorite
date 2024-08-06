package nextstep.favorite.presentation;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.FavoriteService;
import nextstep.member.domain.LoginMember;
import nextstep.member.presentation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody FavoriteRequest request
    ) {
        Long createdFavoriteId = favoriteService.createFavorite(request, loginMember);
        return ResponseEntity
                .created(URI.create("/favorites/" + createdFavoriteId))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
