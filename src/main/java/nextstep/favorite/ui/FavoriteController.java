package nextstep.favorite.ui;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteCreateResponse;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import nextstep.auth.ui.AuthenticationPrincipal;
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
    public ResponseEntity<FavoriteCreateResponse> createFavorite(
        @AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest request
    ) {
        Long favoriteId = favoriteService.createFavorite(loginMember.getEmail(), request);
        return ResponseEntity
            .created(URI.create("/favorites/" + favoriteId))
            .body(new FavoriteCreateResponse(favoriteId, request.getSource(), request.getTarget()));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        List<FavoriteResponse> favorites = favoriteService.findFavoritesByMemberEmail(loginMember.getEmail());
        return ResponseEntity.ok().body(favorites);
    }

    @GetMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> getFavorites(
        @AuthenticationPrincipal LoginMember loginMember,
        @PathVariable Long id
    ) {
        final var favorite = favoriteService.findFavoriteByMemberEmail(loginMember.getEmail(), id);
        return ResponseEntity.ok().body(favorite);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(
        @AuthenticationPrincipal LoginMember loginMember,
        @PathVariable Long id
    ) {
        favoriteService.deleteFavorite(loginMember.getEmail(), id);
        return ResponseEntity.noContent().build();
    }
}
