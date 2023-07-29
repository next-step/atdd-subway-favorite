package nextstep.subway.controller;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.controller.request.FavoriteCreateRequest;
import nextstep.subway.controller.resonse.FavoriteResponse;
import nextstep.subway.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal UserPrincipal principal, @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(principal.getMemberEmail(), favoriteCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal UserPrincipal principal) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(principal.getMemberEmail());
        return ResponseEntity.ok().body(favoriteResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> showFavorite(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        FavoriteResponse favoriteResponse = favoriteService.findFavorite(principal.getMemberEmail(), id);
        return ResponseEntity.ok().body(favoriteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        favoriteService.deleteFavorite(principal.getMemberEmail(), id);
        return ResponseEntity.noContent().build();
    }
}
