package nextstep.subway.favorite.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    @GetMapping("/favorites")
    public ResponseEntity getFavorites() {
        return ResponseEntity.ok().build();
    }
}
