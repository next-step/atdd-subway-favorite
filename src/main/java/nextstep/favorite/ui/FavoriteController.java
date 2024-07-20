package nextstep.favorite.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FavoriteController {
  private final FavoriteService favoriteService;

  @PostMapping("/favorites")
  public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest request) {
    favoriteService.createFavorite(request);
    return ResponseEntity.created(URI.create("/favorites/" + 1L)).build();
  }

  @GetMapping("/favorites")
  public ResponseEntity<List<FavoriteResponse>> getFavorites() {
    List<FavoriteResponse> favorites = favoriteService.findFavorites();
    return ResponseEntity.ok().body(favorites);
  }

  @DeleteMapping("/favorites/{id}")
  public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
    favoriteService.deleteFavorite(id);
    return ResponseEntity.noContent().build();
  }
}
