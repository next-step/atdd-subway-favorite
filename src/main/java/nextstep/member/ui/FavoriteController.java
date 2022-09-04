package nextstep.member.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
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
    @Secured("ROLE_MEMBER")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @RequestBody FavoriteRequest favoriteRequest) {
        Long favoriteId = favoriteService.saveFavorite(loginMember, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    @Secured("ROLE_MEMBER")
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> responses = favoriteService.findFavoriteResponses(loginMember);
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{favoriteId}")
    @Secured("ROLE_MEMBER")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(loginMember, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
