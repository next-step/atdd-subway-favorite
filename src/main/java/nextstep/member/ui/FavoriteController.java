package nextstep.member.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.create(request, userPrincipal.getUsername());
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> response = favoriteService.findFavorites(userPrincipal.getUsername());
        return ResponseEntity.ok().body(response);
    }
}
