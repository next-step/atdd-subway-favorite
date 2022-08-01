package nextstep.favorite.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.userdetails.User;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> saveFavorite(
            @AuthenticationPrincipal final User user,
            @RequestBody final FavoriteRequest favoriteRequest) {

        final FavoriteResponse response = favoriteService.saveFavorite(user.getEmail(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

}

