package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.request.AddFavoriteRequest;
import nextstep.favorite.application.response.ShowAllFavoriteResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;
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
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody AddFavoriteRequest addFavoriteRequest) {
        favoriteService.createFavorite(loginMember, addFavoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + 1L)).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<ShowAllFavoriteResponse> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok().body(favoriteService.findFavorites(loginMember));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
