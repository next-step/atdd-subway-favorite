package nextstep.member.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
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

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody FavoriteRequest request) {
        Favorite favorite = favoriteService.createFavorite(userPrincipal.getUsername(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavoriteList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(favoriteService.getFavorites(userPrincipal.getUsername()));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavoriteList(
            @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id) {
        favoriteService.deleteFavorite(userPrincipal.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
