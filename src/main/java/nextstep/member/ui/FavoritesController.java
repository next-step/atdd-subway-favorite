package nextstep.member.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    private final FavoriteService favoriteService;

    public FavoritesController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping()
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.createFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping()
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> response = favoriteService.findAll(loginMember.getId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<List<FavoriteResponse>> removeFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                                 @PathVariable Long favoriteId) {
        favoriteService.removeFavorite(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}

