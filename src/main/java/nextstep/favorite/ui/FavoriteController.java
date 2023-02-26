package nextstep.favorite.ui;

import java.net.URI;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.Favorites;
import nextstep.favorite.ui.request.FavoriteRequest;
import nextstep.favorite.ui.response.FavoriteResponses;
import nextstep.member.infrastructure.AuthPrincipal;
import nextstep.member.infrastructure.dto.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<String> createFavorites(@AuthPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteService.createFavorite(loginMember, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<FavoriteResponses> getFavorites(@AuthPrincipal LoginMember loginMember) {
        Favorites favorites = favoriteService.getFavorite(loginMember.getId());
        return ResponseEntity.ok(FavoriteResponses.from(favorites));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorites(@AuthPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
