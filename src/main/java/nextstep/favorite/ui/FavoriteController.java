package nextstep.favorite.ui;

import java.util.List;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.FavoriteCreateRequest;
import nextstep.favorite.application.FavoriteResponse;
import nextstep.favorite.application.FavoriteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserPrincipal user,
            @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        long source = favoriteCreateRequest.getSource();
        long target = favoriteCreateRequest.getTarget();
        String email = user.getUsername();
        long favoriteId = favoriteService.createFavorite(email, source, target);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/favorites/" + favoriteId)
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserPrincipal user) {
        String email = user.getUsername();
        List<FavoriteResponse> favoriteResponses = favoriteService.getFavorites(email);
        return ResponseEntity.ok().body(favoriteResponses);
    }
}
