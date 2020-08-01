package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest request) {
        Favorite favorite = favoriteService.createFavorite(loginMember.getId(), request);
        return ResponseEntity
            .created(URI.create("/favorites/" + favorite.getId()))
            .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
