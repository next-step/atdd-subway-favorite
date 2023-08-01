package nextstep.api.favorite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.aop.principal.AuthenticationPrincipal;
import nextstep.api.auth.aop.principal.UserPrincipal;
import nextstep.api.favorite.application.FavoriteService;
import nextstep.api.favorite.application.dto.FavoriteRequest;
import nextstep.api.favorite.application.dto.FavoriteResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal final UserPrincipal user,
                                                           @RequestBody final FavoriteRequest request) {
        final var response = favoriteService.saveFavorite(user.getUsername(), request);
        return ResponseEntity
                .created(URI.create("/favorites/" + response.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal final UserPrincipal user) {
        final var response = favoriteService.findAllFavorites(user.getUsername());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorites(@AuthenticationPrincipal final UserPrincipal user,
                                                @PathVariable("id") final Long favoriteId) {
        favoriteService.deleteFavorite(user.getUsername(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
