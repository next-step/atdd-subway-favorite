package nextstep.subway.ui;

import nextstep.subway.application.dto.favorite.FavoriteRequest;
import nextstep.subway.application.dto.favorite.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class FavoriteController {

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest request) {
        return ResponseEntity.created(URI.create("/favorites/1")).build();
    }

    @GetMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> findFavorite(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}