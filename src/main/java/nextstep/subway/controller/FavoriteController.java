package nextstep.subway.controller;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.controller.request.FavoriteCreateRequest;
import nextstep.subway.controller.resonse.FavoriteResponse;
import nextstep.subway.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal UserPrincipal principal, @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(principal.getUserMail(), favoriteCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> showLine(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        FavoriteResponse favoriteResponse = favoriteService.findFavorite(principal.getUserMail(), id);
        return ResponseEntity.ok().body(favoriteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        favoriteService.findFavorite(principal.getUserMail(), id);
        return ResponseEntity.noContent().build();
    }
}
