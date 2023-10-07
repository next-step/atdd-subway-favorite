package nextstep.member.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.CreateFavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@RequestBody CreateFavoriteRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Favorite favorite = favoriteService.createFavorite(request, userPrincipal.getUsername());
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> findFavorite(@PathVariable Long id) {
        FavoriteResponse favorite = favoriteService.findFavorite(id);
        return ResponseEntity.ok().body(favorite);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
