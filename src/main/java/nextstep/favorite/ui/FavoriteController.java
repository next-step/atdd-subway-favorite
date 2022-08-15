package nextstep.favorite.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorites")
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    public ResponseEntity<FavoriteResponse> saveFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse response = favoriteService.saveFavorite(loginMember.getEmail(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
    }

    @GetMapping("/favorites/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    public ResponseEntity<FavoriteResponse> findFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.findFavorite(loginMember.getEmail(), id));
    }

    @DeleteMapping("/favorites/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MEMBER"})
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember.getEmail(), id);
        return ResponseEntity.noContent().build();
    }
}