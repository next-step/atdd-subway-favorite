package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(
            @RequestBody FavoriteRequest request,
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        Favorite favorite = favoriteService.createFavorite(request, loginMember);
        return ResponseEntity
                .created(URI.create("/favorites/" + favorite.getId())).body(FavoriteResponse.of(favorite));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.deleteFavorite(id,loginMember);
        return ResponseEntity.noContent().build();
    }
}
