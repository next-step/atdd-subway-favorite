package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.auth.user.UserDetails;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Secured(value = {RoleType.ROLE_ADMIN, RoleType.ROLE_MEMBER})
    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal final UserDetails userDetails,
        @RequestBody final FavoriteRequest favoriteRequest) {

        final FavoriteResponse favorite = favoriteService.createFavorite(userDetails, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }

    @Secured(value = {RoleType.ROLE_ADMIN, RoleType.ROLE_MEMBER})
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal final UserDetails userDetails) {
        return ResponseEntity.ok(favoriteService.getFavorites(userDetails));
    }
}
