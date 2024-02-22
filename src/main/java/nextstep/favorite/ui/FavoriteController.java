package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.request.AddFavoriteRequest;
import nextstep.favorite.application.response.AddFavoriteResponse;
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
    public ResponseEntity<AddFavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody AddFavoriteRequest addFavoriteRequest) {
        AddFavoriteResponse favorite = favoriteService.createFavorite(loginMember, addFavoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + 1L)).body(favorite);
    }

    @GetMapping("/favorites")
    public ResponseEntity<ShowAllFavoriteResponse> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok().body(favoriteService.findFavorites(loginMember));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember, id);
        return ResponseEntity.noContent().build();
    }

}
