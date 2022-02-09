package nextstep.domain.subway.api;

import nextstep.auth.authentication.LoginMember;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.domain.member.domain.LoginMemberImpl;
import nextstep.domain.subway.domain.FavoritePath;
import nextstep.domain.subway.dto.FavoritePathRequest;
import nextstep.domain.subway.dto.response.FavoritePathResponse;
import nextstep.domain.subway.service.FavoritePathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritePathController {
    private final FavoritePathService favoritePathService;

    public FavoritePathController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping
    public ResponseEntity<?> createFavoritePath(@AuthenticationPrincipal LoginMemberImpl member, @RequestBody FavoritePathRequest favoritePathRequest) {
        long saveFavoriteId = favoritePathService.createFavorite(member, favoritePathRequest);
        return ResponseEntity.created(URI.create("/favorites/" + saveFavoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoritePathResponse>> showFavorites(@AuthenticationPrincipal LoginMemberImpl member) {
        List<FavoritePathResponse> favorites =  favoritePathService.showFavorites(member);
        return ResponseEntity.ok()
                .body(favorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<?> deleteFavorite(@AuthenticationPrincipal LoginMemberImpl member,
                                            @PathVariable("favoriteId") Long favoriteId) {
        favoritePathService.deleteFavorite(favoriteId);
        return ResponseEntity.noContent().build();
    }

}
