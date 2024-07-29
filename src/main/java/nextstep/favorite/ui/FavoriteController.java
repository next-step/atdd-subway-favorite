package nextstep.favorite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.favorite.application.DefaultFavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;

@RestController
public class FavoriteController {
    private final DefaultFavoriteService favoriteService;

    public FavoriteController(DefaultFavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(
        @RequestBody FavoriteRequest request,
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        Favorite favorite = favoriteService.createFavorite(request, loginMember);
        return ResponseEntity.created(URI.create(getCreatedURIPath(favorite.getId()))).build();
    }

    private static String getCreatedURIPath(Long favoriteId) {
        return "/favorites/" + favoriteId;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(
        @PathVariable Long id,
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        favoriteService.deleteFavorite(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
