package nextstep.favorite.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.favorite.applicataion.FavoriteService;
import nextstep.favorite.applicataion.dto.FavoriteRequest;
import nextstep.favorite.applicataion.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<URI> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                                 @RequestBody FavoriteRequest request) {
        favoriteService.createFavorite(loginMember.getId(), request.getSource(), request.getTarget());
        return ResponseEntity.created(URI.create("/favorites")).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favoriteResponses = favoriteService.getAllFavorites(loginMember.getId());
        return ResponseEntity.ok().body(favoriteResponses);
    }

}
