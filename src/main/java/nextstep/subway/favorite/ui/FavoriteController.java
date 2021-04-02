package nextstep.subway.favorite.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class FavoriteController {

    @PostMapping("/favorites")
    public ResponseEntity create(Long source, Long target) throws URISyntaxException {
        Long id = 1L;
        return ResponseEntity.created(new URI(String.format("/favorites/", id))).build();
    }
}
