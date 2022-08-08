package nextstep.favorite.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.secured.Secured;
import nextstep.auth.user.User;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Secured({"ROLE_ADMIN","ROLE_MEMBER"})
    @PostMapping("/favorites")
    public ResponseEntity<Void> saveFavorite(@AuthenticationPrincipal User user,
        @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse response = favoriteService.saveFavorite(user.getEmail(), favoriteRequest);

        return ResponseEntity.created(URI.create(String.format("/favorites/%s", response.getId())))
            .build();
    }

    @Secured({"ROLE_ADMIN","ROLE_MEMBER"})
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal final User user) {
        List<FavoriteResponse> response = favoriteService.findFavorites(user.getEmail());
        return ResponseEntity.ok(response);
    }

    @Secured({"ROLE_ADMIN","ROLE_MEMBER"})
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(
        @AuthenticationPrincipal final User user,
        @PathVariable final Long id) {

        favoriteService.deleteFavorite(user.getEmail(), id);
        return ResponseEntity.noContent().build();
    }

}
