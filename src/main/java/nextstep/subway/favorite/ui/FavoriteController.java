package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    @PostMapping("/favorites")
    public ResponseEntity createFavorites(@RequestBody FavoriteRequest request) {
        return ResponseEntity.ok().build();
    }
}
