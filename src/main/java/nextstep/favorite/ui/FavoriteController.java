package nextstep.favorite.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.userdetails.User;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal User user,
            @RequestBody FavoriteRequest request
    ) {
        Long id = favoriteService.createFavorite(user.getPrincipal(), request.getSource(), request.getTarget());
        return ResponseEntity.created(URI.create("/favorite/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> searchAll(@AuthenticationPrincipal User user) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavorites(user.getPrincipal());
        return ResponseEntity.ok().body(favoriteResponses);
    }
}
