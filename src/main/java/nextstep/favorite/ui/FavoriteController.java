package nextstep.favorite.ui;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteCreateRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.AuthToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/favorites")
@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> createFavorite(
            @AuthToken String email,
            @RequestBody FavoriteCreateRequest favoriteCreateRequest) {

        Long id = favoriteService.createFavorite(email, favoriteCreateRequest);

        return ResponseEntity.created(
                URI.create("/favorites/" + id)
        ).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthToken String email) {
        return ResponseEntity.ok(favoriteService.getFavorites(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthToken String email, @PathVariable Long id) {
        favoriteService.deleteFavorite(id);

        return ResponseEntity.noContent()
                .build();
    }
}
