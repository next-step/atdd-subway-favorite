package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.dto.FavoriteRequest;
import nextstep.subway.dto.FavoriteResponse;
import nextstep.subway.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorites")
    ResponseEntity createFavorite(@RequestBody FavoriteRequest request) {
        Long favoriteId = favoriteService.createFavorite(request);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping("/favorites")
    ResponseEntity<List<FavoriteResponse>> getFavorites() {
        List<FavoriteResponse> responseList = favoriteService.getFavorites();
        return ResponseEntity.ok()
                .body(responseList);
    }

    @DeleteMapping("/favorites/{id}")
    ResponseEntity deleteFavorite(@PathVariable long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
