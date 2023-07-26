package nextstep.favorite.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.service.FavoritePathService;
import nextstep.favorite.service.dto.FavoritePathRequest;
import nextstep.favorite.service.dto.FavoritePathResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoritePathController {

    private final FavoritePathService favoritePathService;

    @PostMapping
    public ResponseEntity<?> createFavoritePath(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody FavoritePathRequest request) {
        favoritePathService.createFavoritePath(userPrincipal.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoritePathResponse>> getFavoritePaths(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        final List<FavoritePathResponse> response = favoritePathService.getFavoritePaths(userPrincipal.getUsername());

        return ResponseEntity.ok(response);
    }
}
