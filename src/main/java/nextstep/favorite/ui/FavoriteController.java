package nextstep.favorite.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.MemberDetails;
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
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse response = favoriteService.createFavorite(memberDetails, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/favorite")
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    public ResponseEntity<FavoriteResponse> getFavorite(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.getFavorite(memberDetails, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/favorites")
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal MemberDetails memberDetails) {
        List<FavoriteResponse> response = favoriteService.getFavorites(memberDetails);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/favorites/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    public ResponseEntity<FavoriteResponse> deleteFavorite(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable Long id) {
        favoriteService.deleteFavorite(memberDetails, id);
        return ResponseEntity.noContent().build();
    }


}
