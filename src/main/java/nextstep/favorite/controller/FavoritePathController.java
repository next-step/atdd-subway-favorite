package nextstep.favorite.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.service.FavoritePathService;
import nextstep.favorite.service.dto.FavoritePathRequest;
import nextstep.favorite.service.dto.FavoritePathResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoritePathController {

    private final FavoritePathService favoritePathService;

    @PostMapping
    public ResponseEntity<?> createFavoritePath(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody FavoritePathRequest request) {
        final Long favoriteId = favoritePathService.createFavoritePath(userPrincipal.getUsername(), request);

        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoritePathResponse>> getFavoritePaths(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        final List<FavoritePathResponse> response = favoritePathService.getFavoritePaths(userPrincipal.getUsername());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{favoritePathId}")
    public ResponseEntity<?> deleteFavoritePath(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long favoritePathId) {
        favoritePathService.deleteFavoritePath(favoritePathId, userPrincipal.getUsername());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
