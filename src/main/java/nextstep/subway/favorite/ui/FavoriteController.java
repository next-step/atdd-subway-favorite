package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.service.FavoriteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorites")
    public ResponseEntity getFavorites() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/favorites", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FavoriteResponse> createStation(@RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favorite = favoriteService.saveFavorite(favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }
}
