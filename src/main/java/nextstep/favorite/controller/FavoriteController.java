package nextstep.favorite.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.dto.request.FavoriteRequest;
import nextstep.favorite.dto.response.FavoriteResponse;
import nextstep.favorite.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @Valid @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.saveFavorite(userPrincipal.getEmail(), request);
        URI uri = URI.create(String.format("/favorites/%d", favorite.getId()));

        return ResponseEntity
                .created(uri)
                .body(favorite);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<FavoriteResponse> favorites = favoriteService.findAllFavoritesByEmail(userPrincipal.getEmail());

        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long id) {
        favoriteService.deleteFavoriteByFavoriteId(id, userPrincipal.getEmail());

        return ResponseEntity
                .noContent()
                .build();
    }

}
