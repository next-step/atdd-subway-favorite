package nextstep.member.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.CreateFavoriteRequest;
import nextstep.member.domain.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorites(@RequestBody CreateFavoriteRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Favorite favorite = favoriteService.createFavorite(request, userPrincipal.getUsername());
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }
}
