package nextstep.favorite.controller;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.controller.dto.FavoriteRequest;
import nextstep.favorite.controller.dto.FavoriteResponse;
import nextstep.favorite.domain.command.FavoriteService;
import nextstep.favorite.domain.query.FavoriteReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final FavoriteReader favoriteReader;

    @PostMapping("")
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request) {
        favoriteService.createFavorite(request.toCommand());
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
                .build();
    }

    @GetMapping("")
    public ResponseEntity<List<FavoriteResponse>> getFavorites() {
        List<FavoriteResponse> favorites = favoriteReader.findFavorites();
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
