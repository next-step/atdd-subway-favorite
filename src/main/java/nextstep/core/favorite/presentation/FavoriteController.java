package nextstep.core.favorite.presentation;

import nextstep.core.favorite.application.FavoriteService;
import nextstep.core.favorite.application.dto.FavoriteRequest;
import nextstep.core.favorite.application.dto.FavoriteResponse;
import nextstep.core.member.domain.LoginMember;
import nextstep.core.member.presentation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request, @AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.createFavorite(request, loginMember);
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
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
