package nextstep.subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite() {
        return ResponseEntity.created(URI.create("/favorites/1")).build();
    }
}
