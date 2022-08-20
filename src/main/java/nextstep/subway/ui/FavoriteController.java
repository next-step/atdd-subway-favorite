
package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favorite = favoriteService.saveFavorite(loginMember, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }

    @GetMapping("")
    public ResponseEntity<List<FavoriteResponse>> favorites() {
        List<FavoriteResponse> responses = favoriteService.findFavorites();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> updateLine(@PathVariable Long id) {
        favoriteService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

}
